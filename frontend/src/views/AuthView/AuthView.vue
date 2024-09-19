<template>
  <div>
    <!-- <div><img src="https://mustsharenews.com/wp-content/uploads/2024/08/Black-Myth-Wukong.jpg" alt=""></div> -->
    <div></div>
  </div>
</template>


<script>
export default {
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
    }
  }
}
</script>

<style>

</style>
