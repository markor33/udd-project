<script setup lang="ts">

</script>

<template>
  <div class="container mt-5">
    <h2 class="mb-5">Statistics</h2>
    <iframe src="http://localhost:5601/app/dashboards#/view/1bcde7a7-ef1f-4ba3-b030-05e3da413df8?embed=true&_g=(refreshInterval:(pause:!t,value:60000),time:(from:now-1y%2Fd,to:now))&_a=()" height="600" width="800"></iframe>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 70%;
  height: 800px;
}

.container iframe {
  width: 100%;
  height: 100%;
}
</style>
