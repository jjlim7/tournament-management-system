<template>
  <nav
    class="navbar-expand-lg px-4 py-2 backgoundColour rounded-bottom-4"
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
        v-if="isLargeScreen && this.userStore.user.role == 'ROLE_PLAYER'"
        >
        <NavbarLinkSection/>
      </div>

      <div class="d-flex align-items-center">
        <!-- show profile pic -->
        <ProfileLink v-if="this.userStore.user.role == 'ROLE_PLAYER'"/>
        <!-- log out button for admin -->
        <button 
          v-if="this.userStore.user.role == 'ROLE_ADMIN'"
          class="btn btn-secondary d-flex justify-content-end align-items-center py-0 px-1 m-0"
          @click="logout">
          <font-awesome-icon :icon="['fas', 'right-from-bracket']" />
          <div class="px-2">Log Out</div>
        </button>
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
import { useUserStore } from '@/stores/store';


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
    logout(){
      console.log("you have logged out")
      this.userStore.logout();
      this.$router.push('/auth');
    }
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
    },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }
};
</script>

<style scoped>
.backgoundColour{
  background-color: rgba(0, 0, 0, 0.3);
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); 
}


</style>