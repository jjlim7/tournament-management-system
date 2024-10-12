
<!-- 
 This is a modal

 below is the button to call the modal. i need to include the ID of the new modal. in this eg, it is tempmodal

<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#tempmodal">
    Launch modal
</button> 

below is an eg of how to use the modal
<Modal 
    header="hello world" 
    modalID="tempmodal" 
    :showFooter="true" 
    :action="applyBooking" 
    actionName="Book">
      <<<any div/ content i want to insert>>>
</Modal>


-->

<template>
    <!-- if i want to disable cancel by using keyboard-> data-bs-keyboard="false"  -->
    <!-- if i want to disable cancel by pressing background> data-bs-backdrop="static" -->
    <div 
        class="modal fade"
        :id="modalID" 
        tabindex="-1" 
        aria-hidden="false"
        aria-labelledby="modalLabel"
        >

        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header py-3 px-4 border-0">
                    <span v-if="header!=''" class="modal-title fs-5 fw-semibold text-black ml-0 pl-0">-{{header}}</span>
                    <!-- button used to disable aria hidden issue. ignore this -->
                    <button class="p-0 m-0 btn fs-5 fw-semibold text-black" inert >-</button>
                    <button class="btn btn-close bg-secondary rounded-4" data-bs-dismiss="modal" :data-bs-target="'#'+modalID" ></button>
                </div>
                <div class="modal-body text-black border-0">
                    <slot></slot>
                </div>
                <div v-if="showFooter" class="modal-footer border-0">
                    <button v-if="prevModalID" type="button" class="btn btn-secondary fw-semibold" :data-bs-target="'#'+prevModalID" data-bs-toggle="modal">Back</button>
                    <button v-else type="button" class="btn btn-secondary fw-semibold" data-bs-dismiss="modal" :data-bs-target="'#'+modalID">Close</button>
                    <button type="button" class="btn btn-primary fw-semibold" @click="action">{{actionName}}</button>
                </div>
            </div>
        </div>
        
    </div>
    
</template>

<script>
import { Modal as bsModal } from 'bootstrap';

export default {
    name:"Modal",
    props:['header','modalID','showFooter','action','actionName','prevModalID'],
    components:{bsModal},
    methods:{
        // dismissAndGoback(){
        //     // :data-bs-target="'#'+prevModalID" data-bs-toggle="modal"
        //     const thisModal = new bsModal('#'+this.modalID)
        //     thisModal.hide()
        //     thisModal.dispose()
        //     const prevModal = new bsModal('#'+this.prevModalID)
        //     prevModal.toggle()
        // },
    },
    created(){
        // console.log(this.header,this.modalID,this.showFooter,this.action,this.actionName,this.prevModalID)
    },
}
</script>

<style scoped>

</style>