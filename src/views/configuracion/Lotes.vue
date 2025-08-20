<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div style="width: 80%;">
        <TableBar @addItem="addItem" @editItem="editItem" @deleteItem="deleteItem" @refreshTable="refreshTable" />
        <DataTable v-model:selection="selectedItems" :value="items" sortField="id" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id">
            <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
            <Column field="id" header="id" hidden></Column>
            <Column field="lotetypeId" header="lotetypeId" hidden></Column>
            <Column field="cuotatypeId" header="cuotatypeId" hidden></Column>
            <Column field="name" header="Nombre" sortable></Column>
            <Column field="description" header="Descripcion" sortable></Column>
            <Column field="lotetypeName" header="Tipo de Lote" sortable></Column>
            <Column field="cuotatypeName" header="Cuota asignada" sortable>

            </Column>
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

                <Select name="lotetypeId" :options="itemsLoteTypes" optionLabel="name" placeholder="Tipo de lote" fluid
                    style=" margin-bottom: 10px;    margin-top: 10px;" />
                <Message v-if="$form.lotetypeId?.invalid" severity="error" size="small" variant="simple">{{
                    $form.lotetypeId.error.message }}</Message>

                <Select name="cuotatypeId" :options="itemsCuotaTypes" optionLabel="name" placeholder="Cuota asignada"
                    fluid style=" margin-bottom: 10px;  margin-top: 20px;" v-model="selectedCuotaType">

                    <template #value="item">
                        <div class="flex-row gap-3">
                            <div v-if="item.value">{{ item.value.name }} - $ {{ item.value.amount }}</div>
                            <div v-else>Cuota</div>
                        </div>
                    </template>
                    <template #option="item">
                        <div class="flex-row gap-3">
                            <div>{{ item.option.name }}</div>
                            <div>$ {{ item.option.amount }}</div>
                        </div>
                    </template>
                </Select>

                <Message v-if="$form.cuotatypeId?.invalid" severity="error" size="small" variant="simple">{{
                    $form.cuotatypeId.error.message }}</Message>

                <div class="formButtons">
                    <Button type="button" label="Cancelar" severity="secondary" @click="showAdd = false"
                        style="margin-top: 20px;"></Button>
                    <Button type="submit" label="Guardar" style="margin-left: 20px;"></Button>
                </div>
                {{ $form }}
            </Form>
            
        </div>
    </Dialog>


</template>

<script>

import apiService from '@/service/apiService';
import { ref } from 'vue';
import TableBar from '@/components/TableBar.vue';
import { Button, Column, ConfirmPopup, DataTable, Dialog, InputText, Message, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from "primevue/useconfirm";

export default {
    components: {
        DataTable, Column, TableBar, Dialog, InputText, Button, Form, Select, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            selectedCuotaType: ref(),
            metaKey: ref(true),
            items: ref([]),
            itemsLoteTypes: ref([]),
            itemsCuotaTypes: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: null,
                name: '',
                description: '',
                lotetypeId: null,
                cuotatypeId: null,
                users: []
            }
        }
    },
    methods: {
        // llamadas al api
        async getTiposLote() {
            try {
                const response = await apiService.getTiposLote();
                this.itemsLoteTypes = response.data;
            } catch (error) {
                console.error("Failed to fetch Tipos de Lotes:", error);
            }
        },
        async getTiposCuota() {
            try {
                const response = await apiService.getTiposCuota();
                this.itemsCuotaTypes = response.data;
            } catch (error) {
                console.error("Failed to fetch Tipos de Lotes:", error);
            }
        },
        async refreshTable() {
            try {
                const response = await apiService.getLotes();
                this.items = response.data;
            } catch (error) {
                console.error("Failed to fetch Lotes:", error);
            }
        },
        async updateLote() {
            try {
                return (await apiService.updateLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update Lote:", error);
            }
        },
        async createLote() {
            try {
                return (await apiService.createLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create Lote:", error);
            }
        },
        async deleteLote(id) {
            try {
                await apiService.deleteLote(id);
            } catch (error) {
                console.error("Failed to delete Lote:", error);
            }
        },

        // listeners de eventos de la barra de acciones
        addItem() {
            this.titulo = 'Agregar nuevo lote';
            this.getTiposLote();
            this.getTiposCuota();
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.description = '';
            this.initialValues.lotetypeId = null
            this.initialValues.cuotatypeId = null;
            this.initialValues.users = [];
            this.selectedCuotaType = null;
            this.showAdd = true;
            this.newItem = true;
        },
        editItem() {
            if (this.selectedItems.length != 1) {
                this.toast.add({ severity: 'error', summary: 'Select only one item to edit.', life: 3000 });
            } else {
                this.titulo = 'Actualizar lote';
                this.getTiposLote();
                this.getTiposCuota();

                console.log("initialValues " + JSON.stringify(this.selectedItems[0]));
                this.initialValues = this.selectedItems[0];
                console.log("initialValues " + JSON.stringify(this.initialValues));

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
                            this.deleteLote(item.id);
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
            if (!values.lotetypeId) {
                errors.lotetypeId = [{ message: 'Lote Type is required.' }];
            }
            if (!values.cuotatypeId) {
                errors.cuotatypeId = [{ message: 'Cuota is required.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.description = values.description;
            this.initialValues.lotetypeId = values.lotetypeId.id;
            this.initialValues.cuotatypeId = values.cuotatypeId.id;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.toast.add({ severity: 'success', summary: 'Lote actualizado correctamente.', life: 3000 });
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues = await this.createLote();
                    this.items = [...this.items, this.initialValues];
                    this.refreshTable();
                } else {
                    this.updateLote();
                    this.items = this.items.map(item =>
                        item.id === this.initialValues.id ? {
                            ...item,
                            id: this.initialValues.id,
                            name: this.initialValues.name,
                            description: this.initialValues.description,
                            lotetypeId: this.initialValues.lotetypeId,
                            cuotatypeId: this.initialValues.cuotatypeId
                        } : item
                    );
                }
            }
        }
    },
    mounted() {
        this.refreshTable();
    }
}

</script>

<style scope>

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