<template>
    <RouterLink 
        class="nav-link py-0 px-2 d-flex mx-auto " 
        :to="eachLink.routeTo"
        @click="collapseNavbar"
        v-if="hasNoClan"
    >
        <li class="nav-item mx-3" >
            <div :class="checkCurrentPage(eachLink.routeTo)" >{{eachLink.linkName}}</div> 
        </li>
    </RouterLink>
</template>
<script>
import { RouterLink } from 'vue-router';
import { Collapse } from 'bootstrap';
import { useClanStore, useUserStore } from '@/stores/store';

export default {
    name: "NavbarLink",
    components:{
        RouterLink
    },
    props:['eachLink'],
    computed:{
        hasNoClan(){
            if(this.eachLink.linkName == 'CLAN WAR' && this.userStore.user.clan==null){
                return false;
            }
            return true;
        }
    },
    methods:{
        checkCurrentPage(link){
            return this.$route.path.startsWith(link) ? "active-link" : "inactive-link"
        },
        collapseNavbar(){
            const collapseElement = document.getElementsByName('SmallScreenNavBar')[0];
            
            if (collapseElement) {
                // Initialize Bootstrap collapse functionality
                const bsCollapse = new Collapse(collapseElement, {
                    toggle: false // Prevent automatic toggling
                });
                // Manually toggle the collapse
                bsCollapse.toggle();
            }
        }
    },
    setup() {
        const clanStore = useClanStore();
        const userStore = useUserStore();
        return { userStore, clanStore };
    },
    
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


