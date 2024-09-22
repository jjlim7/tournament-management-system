<template>
  <div class="top">
    <div class="row align-items-start">
      <div class="text0">
        <h1 style="text-decoration: underline;">Header div</h1>
        <p>{{ displayText0 }}</p>
      </div>
    </div>
    <div class="row align-items-start">
      <div class="col text-end"> <!-- Aligns the image to the right -->
        <img :src="'https://dl.nowpng.com/2024/07/black-myth-wukong-game.png'" class="wukongbody img-fluid" alt="Wukong Image">
      </div>
    </div>
  </div>

  <div class="mid d-flex justify-content-around">
    <img :src="'https://promotions.lenovo.com/uploads/assets/f848702b3458c766a8af753da6d227d6.png'" class="wukongtitle">
    <div class="row align-items-start">
      <div class="text">
        <h1 style="text-decoration: underline;">Diverse Levels</h1>
        <p>{{ displayText1 }}</p>      
      </div>
      <img :src="'https://149367133.v2.pressablecdn.com/wp-content/uploads/2024/08/Black-Myth-Wukong.jpg'" class="wukongimg1">
    </div>
    
    <div class="row d-flex flex-column-reverse flex-md-row align-items-start">
      <img :src="'https://cdn.mos.cms.futurecdn.net/sgFRba5wiRrXrWh2bVkF5a.jpg'" class="wukongimg2">
      <div class="text">
        <h1 style="text-decoration: underline;">Tough YaoGuai</h1>
        <p>{{ displayText2 }}</p>      
      </div>
    </div>

    <div class="row align-items-start">
      <div class="text">
        <h1 style="text-decoration: underline;">VAST VARIATIONS</h1>
        <p>{{ displayText3 }}</p>      
      </div>
      <img :src="'https://cdn.dlcompare.com/others_jpg/upload/news/image/black-myth-wukong-feature-harder-11c72142-image-11c7212b.jpg.webp'" class="wukongimg1">
    </div>
  </div>

  <div class="footer row align-items-start">
    <div class="footer-column">
      <h3>About the Team</h3>
      <p>We are a team committed to providing high-quality tournaments and matchmaking. Our team is dedicated to meeting your needs with professionalism and efficiency.</p>
    </div>
    <div class="footer-column">
      <h3>About Us</h3>
      <p><a href="zeux">Zeux</a></p>
      <p><a href="portfolio">Portfolio</a></p>
      <p><a href="careers">Careers</a></p>
      <p><a href="contactus">Contact Us</a></p>
    </div>
    <div class="footer-column">
      <h3>Contact Us</h3>
      <p>Email: <a href="fakeemail@fakemail.com">fakeemail@fakemail.com</a></p>
      <p>Phone: +65 9112 6229</p>
      <div class="row align-items-start">
      </div>
    </div>
  </div>
</template>

<style scoped>
.top{
  background-image: url('https://cdn.mos.cms.futurecdn.net/TmezMwAfXatkL7M2TDotkM-1200-80.jpg');
  background-size: cover;
  background-position: center;
  height: 700px;
  overflow: hidden; 
  background-attachment: fixed; /* Keep the background fixed */
}
.mid{
  display: flex;
  flex-direction: column;
  gap: 50px; 
  background: linear-gradient(to bottom,#000000,#424242 );
  overflow-x: hidden;
}
.footer {
  background-color: black;
  display: flex;
  justify-content: space-between;
  padding: 20px;
}
.footer-column {
  flex: 0;
  margin: 20px;
  min-width: 200px;
}

/* .line{
  display: flex;
  align-items: flex-start; 
  gap: 20px; 
} */
.text{
  font-size: 30px;
  line-height: 1.5; 
  color: white;   
  margin: 20px;
  width: 50%;
}
.text0{
  padding: 10px;
  position: absolute;
  font-size: 30px;
  line-height: 1.5; 
  color: white;   
  margin: 20px;
  margin-top: 200px;
  width: 40%;
  z-index: 5;
}

.wukongbody{
  width: 1200px; 
  position: relative; 
  top: 20px; 
  right: 0;
}
.wukongtitle {
  position: relative; 
  width: 500px; 
  margin: 0 auto;
}
.wukongimg1{
  position: relative; 
  /* top: 320px;
  left: 100px; */
  width: 520px;
  margin-left: auto;
  padding-right: 40px;
}
.wukongimg2{
  position: relative; 
  /* top: 320px;
  left: 100px; */
  width: 520px;
  margin-right: auto;
  padding-left: 40px;
}

</style>

<script>
export default {
  data() {
    return {
      displayText0: 'Explore a land of vast wonders. Relive the tale of Journey to the West, rewriting a mythical legend',
      displayText1: 'This is some read-only text that will be displayed in a chunk of the page like a document. You cannot edit this text, and it is just for reading purposes.',
      displayText2: 'Confront Mighty Foes, Old and New Challenge extraordinary adversaries, Clash with legends of yore',
      displayText3: 'Combine your Spells and Abilities. Tailor your Talents and unleash your potential'
    }
  }
}
</script>