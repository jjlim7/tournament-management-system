<template>
  <nav
    class="navbar-expand-lg px-4 py-2 backgoundColour"
  >
    <div class="d-flex justify-content-between"  >
      <div class="d-flex align-items-center" >
        <!-- eatwise logo and dropdown button -->
        <NavbarHamburger/>
        <NavbarLogo /> 
      </div>

      <div
        class="collapse navbar-collapse justify-content-center"
        id="navbarSupportedContent"
        v-if="isLargeScreen"
        >
        <NavbarLinkSection />
      </div>

      <div class="d-flex align-items-center">
        <!-- show profile pic -->
        <ProfileLink />
      </div>
      
    </div>
    <div
        class="collapse navbar-collapse justify-content-center"
        id="navbarSupportedContent"
        name="SmallScreenNavBar"
        v-if="!isLargeScreen"
        >
        <NavbarLinkSection />
    </div>
  </nav>
</template>

<script>
import NavbarLinkSection from './NavbarLinks/NavbarLinkSection.vue';
import ProfileLink from './NavbarLinks/ProfileLink.vue';
import NavbarHamburger from './NavbarLogoAndHumburger/NavbarHamburger.vue';
import NavbarLogo from './NavbarLogoAndHumburger/NavbarLogo.vue';


export default {
  name: 'Navbar',
  components:{
    NavbarLinkSection, ProfileLink, NavbarHamburger, NavbarLogo
  },
  data(){
    return{
      islogin: false,
      isLargeScreen: window.innerWidth >= 992,
    }
  },
  methods:{
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 992;
      },
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
    },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },

};
</script>

<style scoped>
.backgoundColour{
  background-color: rgba(0, 0, 0, 0.4);
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); 
}


</style>