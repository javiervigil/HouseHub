<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div style="width: 90%;">
        <TableBar :showIcons="[1, 0, 1, 1, 1]" @addItem="addItem" @deleteItem="deleteItem" @refreshTable="refreshTable"
            @exportExcel="exportExcel" />

        <DataTable v-model:selection="selectedItems" :value="items" sortField="tipo" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id" editMode="cell" :sortOrder="1"
            @cell-edit-complete="onCellEditComplete" :pt="{
                table: { style: 'min-width: 50rem' },
                column: {
                    bodycell: ({ state }) => ({
                        class: [{ '!py-0': state['d_editing'] }]
                    })
                }
            }">
            <template #empty> No existen datos para esta tabla. </template>
            <template #loading> Cargando informacion.... </template>
            <Column selectionMode="multiple" style="width: 2%"></Column>
            <Column field="tipo" header="Tipo" style="width: 48%">
                <template #body="{ data }">
                    {{ data.tipo }}
                </template>
                <template #editor="{ data }">
                    <Select name="tipos" :options="tipos" v-model="selectedType" optionLabel="value"
                        placeholder="Tipo de lote" fluid style=" margin-bottom: 10px;  margin-top: 10px;"
                        size="small" />
                </template>
            </Column>
            <Column field="name" header="Nombre" style="width: 48%">
                <template #body="{ data }">
                    {{ data.name }}
                </template>
                <template #editor="{ data, field }">
                    <InputText v-model="data.name" autofocus fluid size="small" @keydown.enter="$event.target.blur()"
                        :class="{ 'p-invalid': !isNameValid(data, field) }" />
                    <Message v-if="!data.name" severity="error" size="small" variant="simple">{{ errors.name }}
                    </Message>
                </template>
            </Column>
        </DataTable>
    </div>


    <!-- Add  -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">

                <div class="flex flex-col gap-1">
                    <Select name="tipo" :options="tipos" placeholder="Tipo" optionLabel="value" fluid />
                    <Message v-if="$form.tipo?.invalid" severity="error" size="small" variant="simple">{{
                        $form.tipo.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Nombre" fluid />
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{
                        $form.name.error.message }}</Message>
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
import { Button, Column, ConfirmPopup, DataTable, Dialog, InputText, Message, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from "primevue/useconfirm";

import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

export default {
    props: {
        selectedCondominio: {
            type: Object,
            default: null
        },
    },
    components: {
        DataTable, Column, TableBar, Dialog, InputText, Button, Form, Select, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            selectedType: ref(),
            metaKey: ref(true),
            items: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: 0,
                tipo: '',
                name: '',
                condominio: null,
            },
            errors: {
                name: '',
                tipo: ''
            },
            tipos: [
                { id: 'GASTOS_FIJOS', value: 'Gastos Fijos' },
                { id: 'GASTOS_VARIABLES', value: 'Gastos Variables' },
                { id: 'OTROS', value: 'Otros' }
            ]
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getTiposEgresosByCondominio(this.selectedCondominio.id)).data;
                }
            } catch (error) {
                console.error("Failed to fetch EgresoTypes:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async updateEgresoType() {
            try {
                return (await apiService.updateTipoEgreso(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update EgresoType:", error);
                this.toast.add({ severity: 'error', summary: 'Error al actualizar el registro.', life: 3000 });
            }
        },
        async createEgresoType() {
            try {
                return (await apiService.createTipoEgreso(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create EgresoType:", error);
                this.toast.add({ severity: 'error', summary: 'Error al crear un nuevo registro.', life: 3000 });
            }
        },
        async deleteEgresoType(id) {
            try {
                await apiService.deleteTipoEgreso(id);
                this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                this.selectedItems = null;
                await this.refreshTable();
            } catch (error) {
                console.error("Failed to delete EgresoType:", error);
                this.toast.add({ severity: 'error', summary: 'No se puede borrar el dato, verifique que no tenga dependencias con otros datos.', life: 3000 });
            }
        },

        // listeners de eventos de la barra de acciones
        addItem() {
            if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un tipo de egreso.', life: 3000 });
                return;
            }
            this.titulo = 'Agregar nuevo tipo';
            this.initialValues.id = null;
            this.initialValues.tipo = '';
            this.initialValues.name = '';
            this.selectedType = null;
            this.showAdd = true;
            this.newItem = true;
        },
        deleteItem(event) {
            if (!this.selectedItems || this.selectedItems.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un elemento para borrar.', life: 3000 });
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
                        for (const item of this.selectedItems) {
                            this.deleteEgresoType(item.id);
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
                errors.name = [{ message: 'El campo nombre es requerido.' }];
            } else if (values.name.length > 40) {
                errors.name = [{ message: 'El nombre no puede tener mas de 40 caracteres.' }];
            }
            if (!values.tipo) {
                errors.tipo = [{ message: 'El campo tipo es requerido.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.tipo = values.tipo.id;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues.condominio = this.selectedCondominio;
                    this.initialValues = await this.createEgresoType();
                    this.items = [...this.items, this.initialValues];
                    await this.refreshTable();
                    this.toast.add({ severity: 'success', summary: 'Tipo de egreso actualizado correctamente.', life: 3000 });
                }
            }
        },

        //validacion del editor inline
        async onCellEditComplete(event) {
            let { data, field, newValue, originalValue } = event;
            if (!newValue) {
                newValue = data[field];
            } else {

                console.log('que pasa ? ' + JSON.stringify( this.selectedType));

                if (field == 'tipo' && this.selectedType && this.selectedType.id) {                    
                    data.tipo = this.selectedType.id;
                } else {
                    data[field] = newValue;
                    //data.tipo = this.tipos.find(item => item.id === data.tipo);
                }
                this.initialValues = data;
                this.initialValues.condominio = this.selectedCondominio;
                await this.updateEgresoType();
                await this.refreshTable();
            }
        },
        isNameValid(data, field) {
            if (!data[field]) {
                this.errors.name = 'El nombre es un campo requerido.';
                return false;
            } else if (data[field].length > 40) {
                this.errors.name = 'El nombre no puede tener mas de 40 caracteres.';
                return false;
            }
            return true;
        },

        exportExcel() {
            if (this.items.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {
                //arregla titulo de columnas datos
                const itemsExport = this.items.map(item => {
                    return {
                        Tipo: item.tipo,
                        Nombre: item.name,
                    };
                });

                const worksheet = XLSX.utils.json_to_sheet(itemsExport);
                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, 'Tipos de egresos');

                // Genera el archivo binario
                const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
                const data = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

                // Descarga el archivo
                saveAs(data, 'Tipos de egresos.xlsx');
            }
        }
    },
    watch: {
        selectedCondominio(newValue, oldValue) {
            this.refreshTable();
            this.selectedItems = null;
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