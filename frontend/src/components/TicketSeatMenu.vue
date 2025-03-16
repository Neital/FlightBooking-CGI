<template>
  <div class="seat-selection">
    <h2>Select Your Seat</h2>

    <div v-if="flight && flight.flightSeats.length"
         class="seat-grid"
         :style="{ gridTemplateColumns: `repeat(${columns}, 1fr)` }">
      <div v-for="seat in flight.flightSeats" :key="seat.id"
           class="seat"
           :class="getSeatClass(seat)"
           @click="selectSeat(seat)">
        {{ seat.row }}{{ seat.column }}
      </div>
    </div>
    <p v-else>No seats available.</p>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps({
  flight: {
    type: Object,
    required: true
  }
});

const columns = computed(() => props.flight.plane.colCount);

function getSeatClass(seat) {
  if (seat.seatType === 'LEGROOM') return 'seat-legroom';
  if (seat.seatType === 'EXIT_ROW') return 'seat-exit';
  return 'seat-regular';
}

function selectSeat(seat) {
  console.log("Selected seat:", seat);
}
</script>

<style scoped>
.seat-selection {
  padding: 20px;
  text-align: center;
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

.seat-regular {
  background-color: lightgray;
}

.seat-legroom {
  background-color: yellow;
}

.seat-exit {
  background-color: red;
  color: white;
}
</style>