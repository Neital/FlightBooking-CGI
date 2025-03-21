<script setup>

import { defineProps, defineEmits } from 'vue';
import {differenceInHours, differenceInMinutes, format} from "date-fns";

const props = defineProps({
  flight: {
    type: Object,
    required: true
  }})

const startDate = new Date(props.flight.startDate);
const arrivalDate = new Date(props.flight.arrivalDate);

console.log(props.flight)

const emit = defineEmits(['showDetails']);

const handleDetails = () => {
  console.log(props.flight.plane.seatPrices)
  emit("showDetails", props.flight)
}

</script>

<template v-if="flight">
  <div class="flight-details">

    <div class="inner-details">
      <h2>{{format(startDate, 'HH:mm')}}</h2>
      <p>{{flight.fromAirportCode}}</p>
      <p>{{format(startDate, 'MMMM-dd')}}</p>
    </div>

    <div class="inner-details">
      <h2>-</h2>
      <p>{{differenceInHours(arrivalDate, startDate)}}H{{differenceInMinutes(arrivalDate, startDate)-60*differenceInHours(arrivalDate, startDate)}}m</p>
      <p>{{flight.plane.model}}</p>
    </div>

    <div class="inner-details">
      <h2>{{format(arrivalDate, 'HH:mm')}}</h2>
      <p>{{flight.toAirportCode}}</p>
      <p>{{format(arrivalDate, 'MMMM-dd')}}</p>
    </div>

    <div>
      <p>Price from {{ Math.min(...Object.values(flight.plane?.seatPrices || {})) || "N/A" }}€</p>
      <button @click="handleDetails">See details</button>
    </div>
  </div>
</template>

<style scoped>
.flight-details{
  display: flex;
  gap: 1rem;
  text-align: center;
  margin: 1rem 0;
}
h2 {
  margin: 0;
}
p {
  margin: 0;
}

.inner-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

</style>