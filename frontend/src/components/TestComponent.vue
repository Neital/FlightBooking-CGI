<script setup>
const handleClick = async () => {
  try {
    await fetch("http://localhost:8080/api/test", {
      method: "GET",
      credentials: "include"
    }).then((response) => {
      if (!response.ok) {
        throw new Error('HTTP error ${response.status}');
      }
      return response.json();
    }).then((responseData) => {
      console.log(responseData)
    })
  } catch (error){ console.log({error})}
}
</script>

<template>
  <button v-on:click="handleClick">Fetch data</button>
</template>

<style scoped>

</style>