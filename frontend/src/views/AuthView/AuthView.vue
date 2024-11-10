<template>
  <div class="d-flex justify-content-center align-items-center defineHeight">
    <!-- register/login box -->
    <BlurredBGCard class="m-auto p-2 pb-4" style="min-width:400px;">
      
        <div class="d-flex justify-content-around">
          <div :class="{'text-primary': isRegister}" style="font-weight: bold; cursor: pointer;" @click="switchTab(true)">Register</div>
          <div :class="{'text-primary': !isRegister}" style="font-weight: bold; cursor: pointer;" @click="switchTab(false)">Login</div>
        </div>

        <hr class="mt-1">
        
        <div class="mt-3">
          <!-- username -->
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input type="text" class="form-control" id="username" placeholder="Enter username..." v-model="username">
          </div>

          <!-- password -->
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Enter password..." v-model="password">
          </div>

          <!-- confirm password -->
          <div class="mb-3" v-if="isRegister">
            <label for="confirmPassword" class="form-label">confirm Password</label>
            <input type="password" class="form-control" id="confirmPassword" placeholder="Enter confirm password..." v-model="confirmPassword">
          </div>
          
          <div v-if="isError" class="form-text text-danger text-center m-0 p-0">
            <div v-for="(message, index) in errMessage" :key="index" class="fw-semibold">***{{ message }}***</div>
          </div>

          <div class="d-flex justify-content-center mt-5">
            <button class="btn btn-primary w-75 mx-auto fw-semibold" @click="register">{{ actionName }}</button>
          </div>
          
        </div>
        
    </BlurredBGCard>      
  </div>
</template>


<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import { useUserStore } from '@/stores/store';

export default {
  name: "AuthView",
  components:{ BlurredBGCard },
  data(){
    return{
      isRegister: true,
      username: '',
      password: '',
      confirmPassword: '',
      isError: false,
      errMessage: ["error 1", "error2 "]
    }
  },
  computed:{
    actionName(){
      return this.isRegister ? "Register" : "Login";
    }
  },
  //use cookie for auth
  methods: {
    setCookie() {
      // Set a cookie with name 'myCookie', value 'myValue', and expiration of 1 day
      this.$cookies.set('myCookie', 'myValue', '1d');
      this.$cookies.set('myCookie', 'myValue', {
        expires: '1d',        // Expiration time
        path: '/',             // Path for which the cookie is valid
        domain: 'example.com', // Domain for which the cookie is valid
        secure: true,          // Send cookie only over HTTPS
        sameSite: 'Strict'     // Control cross-site request handling
      });
      console.log('Cookie has been set');
    },
    getCookie() {
      // Get the value of the cookie with name 'myCookie'
      const cookieValue = this.$cookies.get('myCookie');
      console.log('Cookie value:', cookieValue);
    },
    deleteCookie() {
      // Delete the cookie with name 'myCookie'
      this.$cookies.remove('myCookie');
      console.log('Cookie has been deleted');
    },
    switchTab(isRegister) {
      this.isRegister = isRegister;
    },
    register() {
      console.log("Registering with", this.username, this.password, this.confirmPassword);
      this.userStore.setIsAuth();
      this.$router.push('/');
    },
    login() {
      console.log("Logging in with", this.username, this.password);
      this.userStore.setIsAuth();
      this.$router.push('/');
    }
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }
}
</script>

<style scoped>
.defineHeight{
  height: 85vh;
}
</style>
