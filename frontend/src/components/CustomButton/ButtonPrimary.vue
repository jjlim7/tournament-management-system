<template>
    <button 
        :style="customButtonStyle" 
        class="btn btn-primary" 
        @click="action"
    >
    {{ value }}
    </button>
</template>

<script>
export default {
    name:"ButtonPrimary",
    props:['value','action'],
    data(){
        return{
            customButtonStyle: {
                backgroundColor: '#FA9021',
                borderColor: '#FA9021',
                color: '#fff',
                fontWeight: 'bold',
            }
        }
    }
}
</script>

<style>

</style>