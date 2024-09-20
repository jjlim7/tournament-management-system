
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
        :aria-labelledby="modalID" 
        aria-hidden="true"
        >

        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header py-3 px-4 border-0">
                    <span v-if="header!=''" class="modal-title fs-5 fw-semibold text-black">{{header}}</span>
                    <button 
                        type="button" 
                        class="btn-close bg-secondary rounded-5" 
                        data-bs-dismiss="modal" 
                        aria-label="Close"></button>
                </div>
                <div class="modal-body text-black border-0">
                    <slot></slot>
                </div>
                <div v-if="showFooter" class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary fw-semibold" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary fw-semibold" @click="action">{{actionName}}</button>
                </div>
            </div>
        </div>
        
    </div>
    
</template>

<script>
export default {
    name:"Modal",
    props:['header','modalID','showFooter','action','actionName']
}
</script>

<style>

</style>