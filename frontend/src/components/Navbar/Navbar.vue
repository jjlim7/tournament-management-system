<template>
  <nav
    class="navbar-expand-lg mx-4 py-2"
  >
    <div class="d-flex justify-content-between"  >
      <div class="d-flex align-items-center" >
        <!-- eatwise logo and dropdown button -->
        <NavbarHamburger />
        <NavbarLogo /> 
      </div>

      <div
        class="collapse navbar-collapse justify-content-center"
        id="navbarSupportedContent"
        >
        <!-- show main pages -->
        <NavbarLinkSection />
      </div>

      <div class="d-flex align-items-center">
        <!-- show profile pic -->
        <ProfileLink />
      </div>
      
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
      islogin: false
    }
  },

};
</script>

<style scoped>



</style>