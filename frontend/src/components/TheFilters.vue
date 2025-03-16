<template>
  <div class="filters-container">
    <h3>Filter Flights</h3>

    <div class="filter-group">
      <label for="fromLocation">From:</label>
      <select v-model="filters.fromLocation">
        <option value="">Any</option>
        <option v-for="location in uniqueFromLocations" :key="location" :value="location">{{ location }}</option>
      </select>
    </div>

    <div class="filter-group">
      <label for="toLocation">To:</label>
      <select v-model="filters.toLocation">
        <option value="">Any</option>
        <option v-for="location in uniqueToLocations" :key="location" :value="location">{{ location }}</option>
      </select>
    </div>

    <div class="filter-group">
      <label for="startDate">Departure Date:</label>
      <input type="date" v-model="filters.startDate">
    </div>

    <div class="filter-group">
      <label for="duration">Max Duration (hours):</label>
      <input type="number" v-model="filters.maxDuration" min="0">
    </div>

    <div class="filter-group">
      <label for="minPrice">Minimum Price:</label>
      <input type="number" v-model="filters.minPrice" min="0">
    </div>

    <div class="filter-group">
      <label for="maxPrice">Maximum Price:</label>
      <input type="number" v-model="filters.maxPrice" min="0">
    </div>

    <button @click="applyFilters">Apply Filters</button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed } from 'vue';

const emit = defineEmits(['apply-filters']);
const props = defineProps({
  allFlights: Array
});

// Filters state
const filters = ref({
  fromLocation: '',
  toLocation: '',
  startDate: '',
  maxDuration: null,
  minPrice: null,
  maxPrice: null
});

// Compute unique locations dynamically
const uniqueFromLocations = computed(() => {
  if (!props.allFlights || !Array.isArray(props.allFlights)) return [];
  return [...new Set(props.allFlights.map(flight => `${flight.fromAirportCity}, ${flight.fromAirportCountry}`))];
});

const uniqueToLocations = computed(() => {
  if (!props.allFlights || !Array.isArray(props.allFlights)) return [];
  return [...new Set(props.allFlights.map(flight => `${flight.toAirportCity}, ${flight.toAirportCountry}`))];
});

// Emit the selected filters
const applyFilters = () => {
  emit('apply-filters', filters.value);
};
</script>

<style scoped>
.filters-container {
  background: white;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.filter-group {
  margin-bottom: 10px;
}

label {
  font-weight: bold;
  display: block;
}

select, input {
  width: 100%;
  padding: 5px;
  margin-top: 5px;
}

button {
  width: 100%;
  background-color: #007bff;
  color: white;
  padding: 10px;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>