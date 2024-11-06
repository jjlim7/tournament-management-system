<template>
  <div class="d-flex justify-content-center align-items-center defineHeight">
    <!-- register/login box -->
    <BlurredBGCard 
        :key="isRegister"
        class="m-auto p-2 pb-4" 
        style="min-width:400px;" 
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="500">
      
        <div class="d-flex justify-content-around">
          <div :class="{'text-primary': isRegister}" style="font-weight: bold; cursor: pointer;" @click="switchTab(true)">Register</div>
          <div :class="{'text-primary': !isRegister}" style="font-weight: bold; cursor: pointer;" @click="switchTab(false)">Login</div>
        </div>

        <hr class="mt-1">
        
        <div class="mt-3">
          <!-- email -->
          <div class="mb-3">
            <label for="email" class="form-label">email</label>
            <input type="email" class="form-control" id="email" placeholder="Enter email..." v-model="email">
          </div>

          <!-- username -->
          <div class="mb-3" v-if="isRegister">
            <label for="name" class="form-label">username</label>
            <input type="name" class="form-control" id="name" placeholder="Enter username..." v-model="name">
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
          
          <div class="d-flex justify-content-center mt-5">
            <button v-if="isRegister" class="btn btn-primary w-75 mx-auto fw-semibold" @click="register">{{ actionName }}</button>
            <button v-if="!isRegister" class="btn btn-primary w-75 mx-auto fw-semibold" @click="login">{{ actionName }}</button>
          </div>

        </div>
        
    </BlurredBGCard>      
  </div>
</template>


<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import { useUserStore } from '@/stores/store';
import axios from '@/utils/axiosInstance'
import { setAuthToken } from '@/utils/axiosInstance'
import Swal from 'sweetalert2';
import AOS from 'aos';
import 'aos/dist/aos.css';

export default {
  name: "AuthView",
  components:{ BlurredBGCard },
  data(){
    return{
      isRegister: true,
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
    }
  },
  computed:{
    actionName(){
      return this.isRegister ? "Register" : "Login";
    }
  },
  mounted() {
    // Initialize AOS
    AOS.init();
  },
  methods: {
    switchTab(isRegister) {
      this.isRegister = isRegister;
      
      // Refresh AOS to trigger animations on new content
      AOS.refresh();
    },
    async register() {
      console.log("Registering with", this.email,this.name, this.password, this.confirmPassword);
      if(this.password!=this.confirmPassword){
        this.showErrorAlert("","Password Mismatched!");
        return
      }
      try{
        const response = await axios.post(`auth/api/register`, 
            {
              "email": this.email, 
              "password": this.password,
              "name" : this.name,
              "role": "ROLE_PLAYER"
            })
        
        if(response.data.token){
          // get all the user info here!!
          setAuthToken(response.data.token);
          this.userStore.setIsAuth();
          this.$router.push('/');
        }else{
          this.showErrorAlert(response.data.message, "Error Creating the player")
        }
      }catch(error){
        console.log("error logging in. error message:", error)
      }
    },

    async login() {
      console.log("Logging in with", this.email, this.password);
      if(this.email == '' || this.password == ''){
        this.showErrorAlert("Missing Credential", "username / password missing")
      }
      try{
        const response = await axios.post(`auth/api/login`, {"email": this.email, "password": this.password})
        
        if(response.data.token){
          // get all the user info here!!

          setAuthToken(response.data.token);
          this.userStore.setIsAuth();
          this.$router.push('/');
        }else{
          this.showErrorAlert("Incorrect Credential", "username / password incorrect")
        }

      }catch(error){
        console.log("error logging in. error message:", error)

      }
    },
    showErrorAlert(errorMessage, title){
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "error",
            title: title,
            text: errorMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    successMessage(successMessage, title){
        Swal.fire({
            position: "center",
            icon: "success",
            title: title,
            text: successMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
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
