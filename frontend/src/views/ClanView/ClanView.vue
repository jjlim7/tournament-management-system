<template>
  <div class="clanpage">
    <div class="texttitle">
      <h1>Clan Wars Tournaments</h1>
    </div>
    <div class="clantournaments translucent-box">
      <button class="image-button" @click="handleClick">
        <img :src="'https://assetsio.gnwcdn.com/blackmythwukong1.jpg?width=1200&height=1200&fit=bounds&quality=70&format=jpg&auto=webp'" class="clantournimage">
      </button>
      <button class="image-button" @click="handleClick">
        <img :src="'https://c4.wallpaperflare.com/wallpaper/34/309/213/black-myth-wukong-game-science-hd-wallpaper-preview.jpg'" class="clantournimage">
      </button>
      <button class="image-button" @click="handleClick">
        <img :src="'https://c4.wallpaperflare.com/wallpaper/508/158/505/black-myth-wukong-%E6%8F%92%E7%94%BB%E5%B8%88%E5%B1%85%E5%A3%AB-hd-wallpaper-preview.jpg'" class="clantournimage">
      </button>
      <button class="image-button addbutton" @click="handleClick">
        <img src="https://cdn-icons-png.flaticon.com/512/16994/16994904.png" style="height: 100px;">
      </button>
    </div>

    <div class="texttitle">
      <h1>Clan Members</h1>
    </div>
    <div class="clanmemebrs translucent-box">
      <table class="data-table">
      <thead>
        <tr>
          <th>Position</th>
          <th>Profile</th>
          <th>Username</th>
          <th>Rank</th>
          <th>Elo</th>
          <th>Progress</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(person, index) in members" :key="index">
          <td>{{ person.position }}</td>
          <td>
            <img :src="person.profile" class="profile-image" style="height: 100px;"/>
          </td>
          <td>{{ person.username }}</td>
          <td>{{ person.rank }}</td>
          <td>{{ person.elo }}</td>
          <td>{{ person.progress }}</td>
        </tr>
      </tbody>
    </table>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'TranslucentBox',
    methods: {
    handleClick() {
      alert('Image button clicked!'); // Replace with your desired action
    }
  },
  data() {
    return {
      members: [
        { position: 1, profile: 'https://seeklogo.com/images/O/one-piece-kozuki-clan-logo-98DE5338D7-seeklogo.com.png', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTp98gwVWdaVG-Tn8J_o0sVzn3Hi8kSyS8NXA&s', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://64.media.tumblr.com/9a5d075a3533bbb19637ebc05ee572fd/1b94cfa3123e528b-4d/s250x400/8149f85f9fc507222cbed4ee82f5b1e74465ddcf.png', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://forum.truckersmp.com/uploads/monthly_2019_06/imported-photo-186659.thumb.jpeg.7ca80c40fa6e972e04cc2f14f5114d80.jpeg', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://steamavatar.io/img/14773519040Sv21.jpg', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
      ],
    };
  }
  };
</script>

<style scoped>
  .clanpage {
    display: flex; /* Keep the display flex for layout */
    justify-content: center; /* Center child elements */
    align-items: center; /* Optional: center vertically if needed */
    flex-direction: column; /* Optional: keep column direction if necessary */
    margin: 0;
    padding: 0;
    min-height: 100vh; /* Ensure it fills at least the viewport height */
    width: 100%; /* Let it expand with the content */
    background-image: url('https://xgamer-1300342626.cos.ap-singapore.myqcloud.com/media/65bb44b4d7efe5b0be89096108623a54.jpg');
    background-position: center; /* Center the background image */
    background-repeat: no-repeat; /* Prevent repeating */
    background-attachment: fixed;
  }

  .clantournaments{
    height: 20%;
  }
  .clanmembers{
    height: 60%; 
  }

  .texttitle {
    align-self: flex-start; /* Align the title to the left */
    margin-left: 20px; /* Add some margin to the left */
    font-size: 24px; /* Adjust font size as needed */
    color: white; /* Set the text color */
  }
  .translucent-box {
    background-color: rgba(255, 255, 255, 0.5); /* White background with 50% opacity */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow */
    margin: 10px 0; /* Add spacing between boxes */
    overflow-x: auto; /* Enable horizontal scrolling */
    white-space: nowrap; /* Prevent images from wrapping to the next line */
    width: 90%;
  }
  .translucent-bar {
    background-color: rgba(171, 170, 170, 0.5); /* White background with 50% opacity */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow */
    margin: 10px 0; /* Add spacing between boxes */
    overflow-x: auto; /* Enable horizontal scrolling */
    white-space: nowrap; /* Prevent images from wrapping to the next line */
  }
  .data-table {
    width: 100%;
    border-collapse: collapse; /* Collapse borders */
  }
  .data-table th,
  .data-table td {
    padding: 8px;
    text-align: left;
    border: none; /* Remove borders */
  }
  .data-table th {
    background-color: rgba(171, 170, 170, 0.5); /* White background with 50% opacity */
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow */
    margin: 10px 0; /* Add spacing between boxes */
    overflow-x: auto; /* Enable horizontal scrolling */
    white-space: nowrap; /* Prevent images from wrapping to the next line */
  }

  .clantournimage{
    width: 500px;
    height: 299px;
    padding:20px;
  }
  .addbutton{
    background: none; /* Remove default button background */
    border: none; /* Remove default button border */
    padding: 0; /* Remove default padding */
    cursor: pointer; /* Show pointer cursor */
    background: transparent; /* Remove default button background */
  }
  .image-button {
    background: transparent; /* Ensure no background */
    border: none; /* Remove border */
    padding: 0; /* Remove padding */
    cursor: pointer; /* Show pointer cursor */
  }
  .plusboximg {
    height: 150px;
  }
</style>
  