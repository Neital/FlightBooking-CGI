<template>
  <div class="ticket-seat-menu">
    <div class="ticket-menu">
      <h2>Select Tickets and Preferences</h2>

      <div v-for="(ticket, index) in tickets" :key="index" class="ticket-selection">
        <h3>Ticket {{ index + 1 }}</h3>

        <label>Class:</label>
        <select v-model="ticket.seatClass">
          <option value="ECONOMY">Economy - {{ getPrice('ECONOMY') }} €</option>
          <option value="BUSINESS">Business - {{ getPrice('BUSINESS') }} €</option>
          <option value="FIRST_CLASS">First Class - {{ getPrice('FIRST_CLASS') }} €</option>
        </select>

        <p>Price: <strong>{{ getPrice(ticket.seatClass) }} €</strong></p>

        <label>Preferences:</label>
        <div>
          <label><input type="checkbox" value="WINDOW" v-model="ticket.preferences" /> Window</label>
          <label><input type="checkbox" value="LEGROOM" v-model="ticket.preferences" /> Legroom</label>
          <label><input type="checkbox" value="AISLE" v-model="ticket.preferences" /> Aisle</label>
          <label><input type="checkbox" value="EXIT_ROW" v-model="ticket.preferences" /> Exit Row</label>
        </div>

        <button @click="removeTicket(index)">Remove Ticket</button>
      </div>

      <button @click="addTicket">Add Ticket</button>
      <button @click="recommendSeats">Recommend Seats</button>
    </div>

    <div class="seat-menu">
      <h2>Seat Map</h2>
      <div v-if="flight && flight.flightSeats.length"
           class="seat-grid"
           :style="{ gridTemplateColumns: `repeat(${columns+1}, 1fr)`, gridGap: '10px', }">
        <template v-for="(seat, index) in flight.flightSeats" :key="seat.id">
          <div class="seat"
               :class="[getSeatClass(seat), getRecommendedClass(seat)]"
               @click="selectSeat(seat)">
            {{ seat.row }}{{ seat.column }}
          </div>
          <div v-if="shouldInsertMiddleGap(index)" class="middle-gap"></div>
        </template>
      </div>
      <p v-else>No seats available.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps, ref } from 'vue';

const props = defineProps({
  flight: {
    type: Object,
    required: true,
  },
});

const columns = computed(() => props.flight.plane.colCount);
const shouldInsertMiddleGap = (index) => {
  return columns.value % 2 === 0 && (index + 1) % columns.value === columns.value / 2;
};

const seatPrices = computed(() => props.flight.plane.seatPrices);

const tickets = ref([
  { seatClass: 'ECONOMY', preferences: [] },
]);

const recommendedSeats = ref([]);  // List to hold recommended seats

const addTicket = () => {
  tickets.value.push({ seatClass: 'ECONOMY', preferences: [] });
};

const removeTicket = (index) => {
  tickets.value.splice(index, 1);
};

const recommendSeats = async () => {
  const prefsList = tickets.value.map(ticket => ({
    seatClass: ticket.seatClass,
    preferredFeatures: ticket.preferences.slice(),
  }));

  // Await the result of sendRequest and update recommendedSeats
  recommendedSeats.value = await sendRequest(props.flight.flightSeats, prefsList);
  console.log('Recommended Seats:', recommendedSeats.value); // For debugging
};

function getSeatClass(seat) {
  let seatClass = '';

  // Add margins based on features
  if (seat.seat.features && seat.seat.features.includes('EXIT_ROW')) {
    seatClass += ' seat-exit-row';
  } else if (seat.seat.features && seat.seat.features.includes('LEGROOM')) {
    seatClass += ' seat-legroom';
  }

  // If the seat is unavailable, apply dark grey
  if (!seat.available) {
    seatClass += ' seat-unavailable';
    return seatClass;
  }

  // Check seat class and apply respective colors
  switch (seat.seat.seatClass) {
    case 'ECONOMY':
      seatClass += ' seat-economy';
      break;
    case 'BUSINESS':
      seatClass += ' seat-business';
      break;
    case 'FIRST_CLASS':
      seatClass += ' seat-first-class';
      break;
    default:
      seatClass += ' seat-regular';
  }

  return seatClass.trim();
}

function getRecommendedClass(seat) {
  // Flatten the recommendedSeats array to check for matching seat IDs
  const flatRecommendedSeats = recommendedSeats.value.flat(); // Flattens the nested array structure

  // Check if the seat is in the recommendedSeats list by comparing the seat IDs
  if (flatRecommendedSeats.some(s => s.id === seat.id)) {
    return 'recommended-seat';  // Apply the recommended-seat class if the seat is in the list
  }
  return '';  // Return empty string if the seat is not recommended
}

function selectSeat(seat) {
  console.log('Selected seat:', seat);
}

function getPrice(classType) {
  return seatPrices.value[classType] || 0;  // Return price based on class or 0 if undefined
}

const sendRequest = async (seats, prefs) => {
  const requestBody = {
    seats: seats,
    prefs: prefs
  };

  try {
    const response = await fetch('http://localhost:8080/api/seats/recommend', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'  // Send data as JSON
      },
      body: JSON.stringify(requestBody)  // Convert JS object to JSON
    });

    // Check if the request was successful
    if (response.ok) {
      return await response.json();  // Return the recommended seats
    } else {
      console.error('Failed to fetch recommended seats');
      return [];  // Return empty array if request failed
    }
  } catch (error) {
    console.error('Error during fetch:', error);
    return [];  // Return empty array if there was an error
  }
};
</script>

<style scoped>
.ticket-seat-menu {
  padding: 20px;
  text-align: center;
  display: flex;
  gap: 5rem;
}

.ticket-selection {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 10px;
}

.seat-grid {
  display: grid;
  gap: 5px;
  justify-content: center;
  margin-top: 10px;
}

.seat {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  border: 1px solid black;
}

/* Available and unavailable seat styles */
.seat-unavailable {
  background-color: darkgrey;
  cursor: not-allowed; /* Disable cursor on unavailable seats */
}

.seat-economy {
  background-color: lightgrey;
}

.seat-business {
  background-color: lightgreen;
}

.seat-first-class {
  background-color: lightcoral;
}

.seat-regular {
  background-color: #f0f0f0; /* Default for any undefined seatClass */
}

/* Special margins for LEGROOM and EXIT_ROW */
.seat-legroom {
  margin-top: 6px;
}

.seat-exit-row {
  margin-top: 20px;
}

.recommended-seat {
  background-color: yellow;
}

.middle-gap {
  grid-column: span 1;
  width: 15px;
}
</style>
