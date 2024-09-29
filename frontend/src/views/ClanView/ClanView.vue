<template>
<div v-if="gotClan">
  <div class="clanpage">
    <div class="texttitle">
      <h1>Clan Wars Tournaments</h1>
    </div>
    <div class="translucent-box clantournaments">
      <button
      v-for="(button, index) in buttons"
      :key="index"
      class="image-button clantourn"
      :style="{
        backgroundImage: `url(${button.image})`,
      }"
      @click="handleClick"
      > 
        <div class="translucent-box-bottomhalf">
          <p>{{ button.text }}</p>
        </div>
      </button>
      <button class="image-button addbutton" @click="showModal = true">
        <img src="https://cdn-icons-png.flaticon.com/512/16994/16994904.png" style="height: 100px;">
      </button>
    </div>

        <!-- Modal structure -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content">
        <h1>Available Clan Tournaments</h1>
        <div class="modal-body">
          <button
          v-for="(button, index) in buttons"
          :key="index"
          class="image-button clantourn"
          :style="{
            backgroundImage: `url(${button.image})`,
          }"
          @click="handleClick"
          > 
            <div class="translucent-box">
              <p>{{ button.text }}</p>
            </div>
          </button>
        </div>
        <!-- Close button inside the modal -->
        <button @click="showModal = false" class="close-modal-btn">
          <img src="https://static.vecteezy.com/system/resources/thumbnails/011/458/959/small_2x/letter-x-alphabet-in-brush-style-png.png">
        </button>
      </div>
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
</div>

<!-- !!!!!!!!!!!!!!!IF THIS GUY GOT NO CLAN!!!!!!!!!!!!!!!! -->
<div v-else>
  <div class="clanpage">
    <div class="texttitle">
      <h1>List of available public clans to join</h1>
    </div>
    <div class="translucent-box">
      <table class="data-table">
      <thead>
        <tr>
          <th>Clan Icon</th>
          <th>Clan Name</th>
          <th>Members</th>
          <th>Rank</th>
          <th>Elo</th>
          <th>Request</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(clan, index) in availableclans" :key="index">
          <td>
            <img :src="clan.clanicon" class="profile-image" style="height: 100px;"/>
          </td>
          <td>
            <span class="clan-link" @click="showModal = true">
              {{ clan.clanname }}
            </span>
          </td>
          <td>{{ clan.members }}</td>
          <td>{{ clan.rank }}</td>
          <td>{{ clan.elo }}</td>
          <td>
            <button
              :class="{'request': clan.request}"
              @click="toggleRequest(index)"
            >
              {{ clan.request ? 'Requested' : 'Request' }}
            </button>
          </td>
        </tr>
      </tbody>
      </table>
      <div></div>
    </div>

    <!-- Modal structure -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content">
        <h1>Clan Info</h1>
        <div class="modal-body">
          
          <div class="clanmemebrs translucent-box">
            <table class="data-table responsive-table">
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
        <!-- Close button inside the modal -->
        <button @click="showModal = false" class="close-modal-btn">
          <img src="https://static.vecteezy.com/system/resources/thumbnails/011/458/959/small_2x/letter-x-alphabet-in-brush-style-png.png">
        </button>
      </div>
    </div>

  </div>
</div>
</template>

<script>
  export default {
    name: 'TranslucentBox',
    methods: {
      toggleRequest(index) {
        this.availableclans[index].request = !this.availableclans[index].request;
      },
      handleClick() {
        alert('Image button clicked!'); 
      },
    },
  data() {
    return {
      availableclans: [
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/11619/11619566.png', clanname: 'rtyhuJK', members: '3/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/6695/6695008.png', clanname: 'SDfgHjK', members: '4/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/8108/8108364.png', clanname: 'ERtyUIo', members: '2/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://upload.wikimedia.org/wikipedia/commons/d/dd/Clan_Uchiwa.png', clanname: 'ZXcvbn', members: '4/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://i.pinimg.com/736x/06/5e/c9/065ec9a47b6db3d597b4870d29faf428.jpg', clanname: 'We4567*9', members: '1/5', rank: 'Diamond', elo: 889, request: false },
      ],
      members: [
        { position: 1, profile: 'https://seeklogo.com/images/O/one-piece-kozuki-clan-logo-98DE5338D7-seeklogo.com.png', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTp98gwVWdaVG-Tn8J_o0sVzn3Hi8kSyS8NXA&s', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://64.media.tumblr.com/9a5d075a3533bbb19637ebc05ee572fd/1b94cfa3123e528b-4d/s250x400/8149f85f9fc507222cbed4ee82f5b1e74465ddcf.png', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://forum.truckersmp.com/uploads/monthly_2019_06/imported-photo-186659.thumb.jpeg.7ca80c40fa6e972e04cc2f14f5114d80.jpeg', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 1, profile: 'https://steamavatar.io/img/14773519040Sv21.jpg', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
      ],
      buttons: [
        { image: 'https://assetsio.gnwcdn.com/blackmythwukong1.jpg?width=1200&height=1200&fit=bounds&quality=70&format=jpg&auto=webp',
          text: 'Button 1 reaufvjekrvnjsie uishgui sohguieos hgur hgusio efhueiofho' 
        },
        { image: 'https://c4.wallpaperflare.com/wallpaper/34/309/213/black-myth-wukong-game-science-hd-wallpaper-preview.jpg',
          text: 'Button 2 aedji afopwfejvbh jvbhkfhawuiof a lfbdjk haf j afhkvbhrk' 
        },
        { image: 'https://c4.wallpaperflare.com/wallpaper/508/158/505/black-myth-wukong-%E6%8F%92%E7%94%BB%E5%B8%88%E5%B1%85%E5%A3%AB-hd-wallpaper-preview.jpg',
          text: 'Button 3 faenfih4 3yt 7b8wyt78 9hf78 hf8 f87 ' 
        },
      ],
      gotClan: true,
      showModal: false, // Controls visibility of the modal
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

  .clantournaments {
    height: 40vh; /* 20% of the viewport height */
    align-items: center; /* Vertically center the content */
    overflow-x: auto; /* Enable horizontal scrolling */
    overflow-y: hidden;
    display: flex;
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
    width: 90%;
    align-items: center; /* Vertically center items */
    border: none;
    overflow-x: auto;
  }
  .translucent-box-bottomhalf{
    position: absolute;
    bottom: 0; /* Stick to the bottom */
    left: 0;
    height: 45%; /* Take up 40% of the height */
    width: 100%; /* Full width */
    color: white;
    background-color: rgba(29, 29, 29, 0.9); 
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Optional shadow */
    font-size: 30px;
    border: none;
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

  .clantourn {
    width: 500px; /* Set desired width */
    height: 299px; /* Set desired height */
    padding: 20px; /* Adjust padding */
    flex-shrink: 0; /* Prevent shrinking */
    background-size: cover; /* Ensure the image covers the button */
    background-position: center; /* Center the image */
    background-repeat: no-repeat; /* Prevent the image from repeating */
  }
  .clantournimage {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  .addbutton{
    border: none; /* Remove default button border */
    padding: 0; /* Remove default padding */
    cursor: pointer; /* Show pointer cursor */
    background: transparent; /* Remove default button background */
    padding: 20px;
    flex-shrink: 0;
  }
  .image-button {
    position: relative; /* Relative positioning to allow the inner box to be positioned absolutely */
    border: none; /* Remove border */
    cursor: pointer; /* Show pointer cursor */
    margin: 5px;
  }
  .plusboximg {
    height: 150px;
  }




  /* Modal overlay style */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000; /* Ensure it appears above other elements */
    color: black;
  }

  /* Modal content box */
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    width: 80%; /* Set fixed width */
    height: 80%; /* Set fixed height */
    text-align: center;
    overflow: hidden; /* Hide overflow in the content area */
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  /* Scrollable content inside modal */
  .modal-body {
    overflow-y: auto; /* Enable vertical scrolling */
    height: 200px; /* Set the scrollable area height */
    margin-bottom: 10px; /* Space between text and close button */
  }

  /* Button styles */
  .open-modal-btn,
  .close-modal-btn {
    padding: 10px 20px;
    background-color: grey;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .open-modal-btn:hover,
  .close-modal-btn:hover {
    background-color: darkgray;
  }

  button {
    padding: 5px 10px;
    border: none;
    cursor: pointer;
  }
  button.clicked {
    background-color: #28a745; 
    color: white;
  }
  button:hover {
    background-color: #007bff; 
    color: white;
  }
  .close-modal-btn {
    position: absolute;
    top: 10px; /* Adjust as needed */
    right: 10px; /* Adjust as needed */
    background: none;
    border: none;
    cursor: pointer;
  }
  .close-modal-btn img {
    width: 24px; /* Adjust the size of the 'X' icon */
    height: 24px;
  }

  .clan-link {
    text-decoration: underline;  /* Adds underline to the text */
    cursor: pointer;             /* Changes cursor to pointer */
    color: inherit;              /* Keeps text color the same as surrounding text */
    border: none;
    background: none;
  }
  .clan-link:hover {
    color: #007bff;              /* Optional: Change color on hover for better UX */
    text-decoration: underline;
}
</style>
  