<template>
  <div class="clanpage">
    <div class="clantournaments translucent-box">
      <h1>Clan Wars Tournaments</h1>
    </div>

    <div class="clanmemebrs translucent-box">
      <h1>Clan Members</h1>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'TranslucentBox',
  };
</script>

<style scoped>
  .clanpage{
    flex-direction: column; /* Stack boxes vertically */
    display: flex;
    justify-content: center; /* Centers horizontally */
    align-items: center; /* Centers vertically */
    height: 100vh; /* Full viewport height */
    margin: 0;
    padding: 0;
    min-height: 100vh; /* Ensure it fills at least the viewport height */
    width: 100%; /* Let it expand with the content */
    background-image: url('https://xgamer-1300342626.cos.ap-singapore.myqcloud.com/media/65bb44b4d7efe5b0be89096108623a54.jpg');
    background-position: center; /* Center the background image */
    background-repeat: no-repeat; /* Prevent repeating */
  }

  .clantournaments{

  }
  .clanmembers{
  }

  .translucent-box {
    background-color: rgba(255, 255, 255, 0.5); /* White background with 50% opacity */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow */
    width: 300px;
    height: 150px;
  }
</style>
  