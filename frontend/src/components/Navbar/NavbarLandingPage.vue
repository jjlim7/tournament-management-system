<template>
  <nav class="mx-4 py-2">
    <div class="d-flex justify-content-between align-items-center"  >
      <NavbarLogo /> 
      <ButtonPrimary :action="navigateToAuth" value="Login / Register"></ButtonPrimary>
    </div>
  </nav>
</template>

<script>
import NavbarLogo from './NavbarLogoAndHumburger/NavbarLogo.vue';
import ButtonPrimary from '../CustomButton/ButtonPrimary.vue';

export default {
  name: 'Navbar',
  components:{
     NavbarLogo, ButtonPrimary
  },
  data(){
    return{
      islogin: false,
    }
  },
  methods:{
    navigateToAuth(){
      this.$router.push('/auth');
    }
  }
};
</script>

<style scoped>

</style>