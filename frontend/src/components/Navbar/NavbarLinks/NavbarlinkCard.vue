<template>
    <RouterLink 
        class="nav-link py-0 px-2 d-flex mx-auto " 
        aria-current="page" 
        :to="eachLink.routeTo"
    >
        <li class="nav-item mx-3" >
            <div :class="checkCurrentPage(eachLink.routeTo)" >{{eachLink.linkName}}</div> 
        </li>
    </RouterLink>
</template>
<script>
import { RouterLink } from 'vue-router';

export default {
    name: "NavbarLink",
    components:{
        RouterLink
    },
    props:['eachLink'],
    methods:{
        checkCurrentPage(link){
            return this.$route.path.startsWith(link) ? "active-link" : "inactive-link"
        }
    }
}
</script>

<style scoped>
.active-link {
    font-weight: bold;
    border-bottom: 3px solid #FA9021; /* Underline effect */
    padding-bottom: 5px; /* Adjust as needed */
}
.inactive-link {
    font-weight: 600;
    padding-bottom: 5px; /* Adjust as needed */
    border-bottom: 3px solid transparent; /* Underline effect */
}
</style>


