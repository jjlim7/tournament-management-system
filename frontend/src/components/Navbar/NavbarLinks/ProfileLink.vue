<template>
    <ul class="navbar-nav mb-lg-0 text-center">
      <!-- <NavbarSearch /> -->
      <RouterLink class="nav-link active py-0 d-flex mx-auto " to="/profile">
        <li class="nav-item" >
          <font-awesome-icon :icon="['fas', 'user']" :class="highlighLink"/>
        </li>
      </RouterLink>
    </ul>
  </template>
  
  <script>
  
  export default {
    name: "NavbarLinkSection1",
    components: {
      
    },
    data() {
      return {
        
      };
    },
    computed:{
      highlighLink(){        
        return this.$route.path.startsWith('/profile')   ? 'activeIcon' : '';
      }
    }
  };
  </script>
  
  <style scoped>
  .activeIcon {
    color: #FA9021;
  }
  </style>
  