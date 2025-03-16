<template>
  <div class="footer">
    <p>&copy; 2025 Flight Booking App. All rights reserved.</p>
    <p>Contact us: support@flightbooking.com</p>
  </div>
</template>

<script>
export default {
  name: 'FooterComponent',
};
</script>

<style scoped>
.footer {
  background-color: #333;
  color: white;
  padding: 20px;
  text-align: center;
  position: fixed;
  width: 100%;
  bottom: 0;
}

.footer p {
  margin: 5px 0;
}

.footer p:first-child {
  font-weight: bold;
}
</style>