<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div class="leftcontent">
        <TableBar @addItem="addItem" @editItem="editItem" @deleteItem="deleteItem" @refreshTable="refreshTable" />
        <DataTable v-model:selection="selectedItems" :value="items" sortField="id" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id"
            v-model:expandedRows="expandedRows" @rowExpand="onRowExpand" @rowCollapse="onRowCollapse">
            <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
            <Column expander style="width: 5rem" />
            <Column field="id" header="Id" hidden></Column>
            <Column field="name" header="Nombre" sortable></Column>
            <Column field="description" header="Descripcion" sortable></Column>
            <Column field="image" header="Imagen" sortable>
                <template #body="slotProps">
                    <img :src="`data:image/jpeg;base64,${slotProps.data.image}`" :alt="slotProps.data.image"
                        class="w-24 rounded" style="width: 70px;" />
                </template>
            </Column>


            <template #expansion="slotProps">
                <div class="p-4">
                    <h5>Usuarios</h5>
                    <DataTable :value="slotProps.data.users">
                        <Column field="id" header="Id" hidden></Column>
                        <Column field="name" header="Customer" sortable></Column>
                        <Column field="email" header="Date" sortable></Column>                        
                    </DataTable>
                </div>
            </template>


        </DataTable>
    </div>


    <!-- Add / Edit item -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">
                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Nombre" fluid />
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{
                        $form.name.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="description" type="text" placeholder="Descripcion" fluid />
                    <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">{{
                        $form.description.error.message }}</Message>
                </div>


                <div class="flex flex-col gap-1">
                    <FileUpload mode="basic" @select="onFileSelect" customUpload auto severity="secondary"
                        class="p-button-outlined" style="margin-bottom: 20px;" />
                    <img v-if="src" :src="src" alt="Imagen" class="shadow-md rounded-xl w-full sm:w-64"
                        style="width: 200px;" />
                </div>

                <div class="formButtons">
                    <Button type="button" label="Cancelar" severity="secondary" @click="showAdd = false"
                        style="margin-top: 20px;"></Button>
                    <Button type="submit" label="Guardar" style="margin-left: 20px;"></Button>
                </div>
            </Form>
        </div>
    </Dialog>
</template>

<script>

import apiService from '@/service/apiService';
import { ref } from 'vue';
import TableBar from '@/components/TableBar.vue';
import { Button, Column, ConfirmPopup, DataTable, Dialog, FileUpload, InputText, Message, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from "primevue/useconfirm";


export default {
    components: {
        DataTable, Column, TableBar, Dialog, InputText, Button, Form, Select, Message, ConfirmPopup, Toast, FileUpload
    },
    data() {
        return {
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            metaKey: ref(true),
            items: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: 0,
                name: '',
                description: '',
                image: null
            },
            //para la imagen
            file: ref(null),
            reader: ref(null),
            src: ref(null),
            //para la tabla anidada
            expandedRows: {}
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                const response = await apiService.getCondominios();
                this.items = response.data;
            } catch (error) {
                console.error("Failed to fetch Condominios:", error);
            }
        },
        async updateCondominio() {
            try {
                return (await apiService.updateCondominio(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update Condominios:", error);
            }
        },
        async createCondominio() {
            try {

                return (await apiService.createCondominio(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create Condominio:", error);
            }
        },
        async deleteCondominio(id) {
            try {
                await apiService.deleteCondominio(id);
            } catch (error) {
                console.error("Failed to delete Condominio:", error);
            }
        },


        // listeners de eventos de la barra de acciones
        addItem() {
            this.titulo = 'Agregar nuevo condominio';
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.description = '';
            this.initialValues.image = null;
            this.src = null;
            this.showAdd = true;
            this.newItem = true;
        },
        editItem() {
            if (this.selectedItems.length != 1) {
                this.toast.add({ severity: 'error', summary: 'Select only one item to edit.', life: 3000 });
            } else {
                this.titulo = 'Actualizar condominio';
                this.initialValues = this.selectedItems[0];
                this.src = "data:image/jpeg;base64," + this.initialValues.image;
                this.showAdd = true;
                this.newItem = false;
            }
        },
        deleteItem(event) {
            if (this.selectedItems.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Select at least one item to delete.', life: 3000 });
            } else {
                this.confirm.require({
                    target: event.currentTarget,
                    message: 'Desea borrar el/los registros?',
                    icon: 'pi pi-info-circle',
                    rejectProps: {
                        label: 'Cancelar',
                        severity: 'secondary',
                        outlined: true
                    },
                    acceptProps: {
                        label: 'Borrar',
                        severity: 'danger'
                    },
                    accept: () => {
                        this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                        for (const item of this.selectedItems) {
                            this.deleteCondominio(item.id);
                            this.items = this.items.filter(itemx => itemx.id !== item.id);
                        }
                    },
                    reject: () => {
                    }
                });
            }
        },

        // Acciones del Form 
        resolver({ values }) {
            const errors = {};
            if (!values.name) {
                errors.name = [{ message: 'Name is required.' }];
            }
            if (!values.description) {
                errors.description = [{ message: 'Description is required.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.description = values.description
            this.initialValues.image = values.image
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.toast.add({ severity: 'success', summary: 'Condominio actualizado correctamente.', life: 3000 });
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues.image = this.src.split(',')[1];
                    this.initialValues.src = this.image;
                    this.initialValues = await this.createCondominio();
                    this.items = [...this.items, this.initialValues];
                    this.refreshTable();
                } else {
                    this.initialValues.image = this.src.split(',')[1];
                    this.initialValues.src = this.image;
                    this.updateCondominio();
                    this.items = this.items.map(item =>
                        item.id === this.initialValues.id ? {
                            ...item,
                            id: this.initialValues.id,
                            name: this.initialValues.name,
                            description: this.initialValues.description,
                        } : item
                    );
                }
            }
        },
        onFileSelect(event) {
            const file = event.files[0];
            const reader = new FileReader();
            reader.onload = async (e) => {
                this.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    },

    mounted() {
        this.refreshTable();
    }
}

</script>

<style scope>
.leftcontent {
    width: 70%;
}

input,
select {
    margin-bottom: 10px;
    margin-top: 10px;
}

.formButtons {
    align-items: center;
    align-content: center;

}
</style>