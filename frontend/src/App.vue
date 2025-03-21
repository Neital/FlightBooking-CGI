<template>
  <div id="app">
    <header-component></header-component>

    <div class="main-container">

      <div class="left-column">
        <flights-view @flight-selected="selectFlight"></flights-view>
      </div>

      <div v-if="selectedFlight" class="right-column">
        <ticket-seat-menu :flight="selectedFlight"></ticket-seat-menu>
      </div>
    </div>

    <footer-component></footer-component>
  </div>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
import FooterComponent from './components/FooterComponent.vue';
import FlightsView from './components/FlightsView.vue';
import TicketSeatMenu from './components/TicketSeatMenu.vue';

export default {
  name: 'App',
  components: {
    HeaderComponent,
    FooterComponent,
    FlightsView,
    TicketSeatMenu
  },
  data() {
    return {
      selectedFlight: null
    };
  },
  methods: {
    selectFlight(flight) {
      this.selectedFlight = flight;
    }
  }
};
</script>

<style scoped>

.main-container {
  display: flex;
  justify-content: space-evenly;
}

</style>