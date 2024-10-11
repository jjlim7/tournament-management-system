<template>
  <div>
    <div class="top d-flex align-items-center">
      <div class="m-5">      
        <h1 style="text-decoration: underline;" class="fw-semibold">Header div</h1>
        <p>{{ displayText0 }}</p>
      </div>
      <div class="p-0 m-0 d-none d-sm-block">
        <img :src="'https://dl.nowpng.com/2024/07/black-myth-wukong-game.png'" class="img-fluid p-0 m-0" alt="Wukong Image">
      </div>
    </div>
  
    <div class="p-4 pb-5">
      <img :src="'https://promotions.lenovo.com/uploads/assets/f848702b3458c766a8af753da6d227d6.png'" class="wukongtitle">
  
      <div class="row mb-4">
        <div class="col-12 col-md-4">
          <h1 style="text-decoration: underline;">Diverse Levels</h1>
          <p>{{ displayText1 }}</p>
        </div>
        <img 
          :src="'https://149367133.v2.pressablecdn.com/wp-content/uploads/2024/08/Black-Myth-Wukong.jpg'" 
          class="col-12 col-md-8 ">
      </div>

      <div class="row mb-4">
        <div class="col-12 col-md-4 order-1 order-md-2">
          <h1 style="text-decoration: underline;">Tough YaoGuai</h1>
          <p>{{ displayText2 }}</p>
        </div>
        <img 
          :src="'https://cdn.mos.cms.futurecdn.net/sgFRba5wiRrXrWh2bVkF5a.jpg'" 
          class="col-12 col-md-8 order-2 order-md-1">
      </div>

      <div class="row mb-4">
        <div class="col-12 col-md-4">
          <h1 style="text-decoration: underline;">VAST VARIATIONS</h1>
          <p>{{ displayText3 }}</p>
        </div>
        <img 
          :src="'https://cdn.dlcompare.com/others_jpg/upload/news/image/black-myth-wukong-feature-harder-11c72142-image-11c7212b.jpg.webp'" 
          class="col-12 col-md-8">
      </div>
  
    </div>
  
    <div class="bg-black d-flex flex-column flex-sm-row justify-content-between">
      <div class="m-2 col-12 col-sm-6">
        <h3>About the Team</h3>
        <p>We are a team committed to providing high-quality tournaments and matchmaking. Our team is dedicated to meeting your needs with professionalism and efficiency.</p>
      </div>
      <div class="m-2 col">  
        <h3>About Us</h3>
        <p><a href="zeux">Zeux</a></p>
        <p><a href="portfolio">Portfolio</a></p>
        <p><a href="careers">Careers</a></p>
        <p><a href="contactus">Contact Us</a></p>
      </div>
      <div class="m-2 col">
        <h3>Contact Us</h3>
        <p>Email: <a href="fakeemail@fakemail.com">fakeemail@fakemail.com</a></p>
        <p>Phone: +65 9112 6229</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.top {
  background-image: url('https://cdn.mos.cms.futurecdn.net/TmezMwAfXatkL7M2TDotkM-1200-80.jpg');
  background-size: cover;
}


.wukongtitle {
  display: block; /* Make it a block-level element */
  margin: 0 auto; /* Centers the image horizontally */
  width: 40%; /* Makes the image responsive */
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