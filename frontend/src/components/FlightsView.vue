<template>
  <div class="flights-view">

    <!-- Filters Component -->
    <TheFilters :allFlights="flights" @apply-filters="updateFilters" />
    <div>
      <h2>Available Flights</h2>
    <div v-if="filteredFlights.length">
      <FlightComponent
          v-for="flight in filteredFlights"
          :key="flight.id"
          :flight="flight"
          @click="selectFlight(flight)"
      />
    </div>
    <p v-else>No flights match the filters.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeMount, ref, defineEmits } from 'vue';
import FlightComponent from "@/components/FlightComponent.vue";
import TheFilters from "@/components/TheFilters.vue";

const emit = defineEmits(['flight-selected']);
const flights = ref([]);
const filters = ref({});

onBeforeMount(() => {
  fetch("http://localhost:8080/api/flights", { method: "GET", credentials: "include" })
      .then(response => response.json())
      .then(data => flights.value = data)
      .catch(error => console.error('Error fetching flights:', error));
});
// Filter flights dynamically
const filteredFlights = computed(() => {
  return flights.value.filter(flight => {
    if (filters.value.fromLocation && !`${flight.fromAirportCity}, ${flight.fromAirportCountry}`.includes(filters.value.fromLocation)) {
      return false;
    }
    if (filters.value.toLocation && !`${flight.toAirportCity}, ${flight.toAirportCountry}`.includes(filters.value.toLocation)) {
      return false;
    }
    if (filters.value.startDate && !flight.startDate.startsWith(filters.value.startDate)) {
      return false;
    }
    if (filters.value.maxDuration) {
      const startTime = new Date(flight.startDate);
      const endTime = new Date(flight.arrivalDate);
      const duration = (endTime - startTime) / (1000 * 60 * 60);
      if (duration > filters.value.maxDuration) {
        return false;
      }
    }
    if (filters.value.minPrice) {
      const minSeatPrice = Math.min(...Object.values(flight.plane?.seatPrices || {})); // Get the minimum seat price
      if (minSeatPrice < filters.value.minPrice) {
        return false;
      }
    }

    if (filters.value.maxPrice) {
      const minSeatPrice = Math.min(...Object.values(flight.plane?.seatPrices || {})); // Get the minimum seat price
      if (minSeatPrice > filters.value.maxPrice) {
        return false;
      }
    }

    return true;
  });
});

// Update filters when user applies them
const updateFilters = (newFilters) => {
  filters.value = newFilters;
}

function selectFlight(flight) {
  emit('flight-selected', flight);
}

</script>

<style scoped>
.flights-view {
  background-color: #fff;
  padding: 10px;
  display: flex;
  gap: 2rem;
}
</style>