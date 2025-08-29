<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div>
        <Button icon="pi pi-plus" variant="outlined" label="Agregar egreso" @click="addEgreso"
            style="margin-right: 10px;" />
    </div>
    <br />

    <div style="width: 90%;">
        <TableBar :showIcons="[0, 0, 1, 1, 1]" @deleteItem="deleteItem" @refreshTable="refreshTable"
            @exportExcel="exportExcel" />

        <DataTable v-model:selection="selectedItems" :value="items" stripedRows size="small" :metaKeySelection="metaKey"
            dataKey="id" :sortOrder="1" selectionMode="multiple">
            <template #empty> No existen datos para esta tabla. </template>
            <template #loading> Cargando informacion.... </template>

            <Column selectionMode="multiple" style="width: 1%;"></Column>
            <Column field="date" header="Fecha">
                <template #body="slotProps">
                    {{ slotProps.data.date }}
                </template>
            </Column>
            <Column field="egresoType.tipo" header="Tipo">
            </Column>
            <Column field="egresoType.name" header="Tipo de transaccion" style="width: 20%;">
            </Column>
            <Column field="concept" header="Concepto">
            </Column>
            <Column field="formaPago" header="Forma de pago">
            </Column>
            <Column header="Cargo">
                <template #body="slotProps">
                    <div>
                        {{ formatCurrency(slotProps.data.ammount) }}
                    </div>
                </template>
            </Column>
            <Column field="fileName" header="Archivo Adjunto">

            </Column>

            <ColumnGroup type=" footer">
                <Row>
                    <Column footer="Totales:" :colspan="4" footerStyle="text-align:right" />
                    <Column>
                        <template #footer="slotProps">
                            <!-- <div><b>{{ formatCurrency(total()) }}</b></div> -->
                        </template>
                    </Column>
                </Row>
            </ColumnGroup>
        </DataTable>
    </div>

    <!-- Add  -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">


                <div class="flex flex-col gap-1">
                    <DatePicker name="date" showIcon fluid iconDisplay="input" dateFormat="dd M yy"
                        v-model="selectedDate" size="small" showButtonBar />
                    <Message v-if="$form.date?.invalid" severity="error" size="small" variant="simple">{{
                        $form.date.error.message }}</Message>
                </div>

                <Select name="formaPago" :options="formaPagoOptions" optionLabel="label" placeholder="Forma de pago"
                    fluid style=" margin-bottom: 10px;  margin-top: 20px;" v-model="selectedFormaPago" size="small">
                    <template #option="item">
                        <div class="flex-row gap-3">
                            <div>{{ item.option.label }}</div>
                        </div>
                    </template>
                </Select>
                <Select name="egresoType" :options="itemsEgresoTypes" optionLabel="name" placeholder="Tipo" fluid
                    style=" margin-bottom: 10px;  margin-top: 20px;" v-model="selectedEgresoType" size="small">
                    <template #option="item">
                        <div class="flex-row gap-3">
                            <div>{{ item.option.tipo }} - {{item.option.name  }}</div>
                        </div>
                    </template>
                </Select>

                <div class="flex flex-col gap-1">
                    <InputText name="ammount" type="number" placeholder="Monto" fluid size="small" />
                    <Message v-if="$form.ammount?.invalid" severity="error" size="small" variant="simple">{{
                        $form.ammount.error.message }}</Message>
                </div>


                <div class="flex flex-col gap-1">
                    <InputText name="concept" type="text" placeholder="Concepto" fluid size="small" />
                    <Message v-if="$form.concept?.invalid" severity="error" size="small" variant="simple">{{
                        $form.concept.error.message }}</Message>
                </div>

                <div class="flex flex-col gap-1">
                    Adjuntar archivo:
                    <FileUpload mode="basic" @select="onFileSelect" customUpload auto severity="secondary"
                        class="p-button-outlined" style="margin-bottom: 20px;" :maxFileSize="1000000"
                        uploadLabel="Abrir" accept="application/pdf">

                    </FileUpload>
                    <!-- <img v-if="src" :src="src" alt="Imagen" class="shadow-md rounded-xl w-full sm:w-64"
                        style="width: 200px;" /> -->
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
import { Button, Column, ColumnGroup, ConfirmPopup, DataTable, DatePicker, Dialog, FileUpload, InputText, Message, Row, Select, Toast } from 'primevue';
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
        DataTable, Column, ColumnGroup, Row, TableBar, Dialog, InputText, Button, Form, Select, DatePicker, FileUpload, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            selectedLote: null,
            items: ref([]),
            selectedFormaPago: ref(),
            selectedEgresoType: ref(),
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            selectedDate: null,
            itemsEgresoTypes: ref([]),
            metaKey: ref(true),
            items: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: 0,
                tipo: 0,
                name: '',
                date: null,
                ammount: 0,
                concept: '',
                loteId: null
            },
            formaPagoOptions: [
                { label: 'Transferencia bancaria', value: 'TRANSFERENCIA' },
                { label: 'Deposito bancario', value: 'DEPOSITO_BANCARIO' },
                { label: 'Efectivo', value: 'EFECTIVO' },
                { label: 'Tarjeta de credito', value: 'TARJETA_CREDITO' },
                { label: 'Tarjeta de debito', value: 'TARJETA_DEBITO' },
                { label: 'Cheque', value: 'CHEQUE' }
            ],
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getEgresosByCondominio(this.selectedCondominio.id)).data;
                    this.itemsEgresoTypes = (await apiService.getTiposEgresosByCondominio(this.selectedCondominio.id)).data;
                }
            } catch (error) {
                console.error("Failed to fetch Egresos:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },

        async updateEgreso() {
            try {
                return (await apiService.updateEgreso(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update Egresos:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async createEgreso() {
            try {
                return (await apiService.createEgreso(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create Egresos:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async deleteEgreso(id) {
            try {
                await apiService.deleteEgreso(id);
                this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                this.selectedItems = null;
                await this.refreshTable();
            } catch (error) {
                console.error("Failed to delete Egresos:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },


        formatDate(value) {
            const dateValue = new Date(value);
            return dateValue.toLocaleDateString('es-MX', {
                day: '2-digit',
                month: 'short',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        },
        formatDateShort(value) {
            const dateValue = new Date(value);
            return dateValue.toLocaleDateString('es-MX', {
                day: '2-digit',
                month: 'short',
                year: 'numeric'

            });
        },
        formatCurrency(value) {
            return '$ ' + value.toLocaleString('es-MX', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        },


        // listeners de eventos de la barra de acciones
        async addEgreso() {
            if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un abono.', life: 3000 });
                return;
            }
            this.titulo = 'Agregar egreso';
            this.initialValues.id = null;
            this.initialValues.tipo = 1;
            this.initialValues.concept = '';
            this.initialValues.ammount = '';
            this.selectedDate = null;
            this.initialValues.date = this.formatDateShort(new Date());
            this.initialValues.type = 'ABONO';
            this.selectedType = null;
            this.showAdd = true;
            this.newItem = true;
        },

        editItem() {
            if (this.selectedItems.length != 1) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un elemento de la lista.', life: 3000 });
            } else {
                this.titulo = 'Actualizar cuota';
                this.initialValues = this.selectedItems[0];
                this.showAdd = true;
                this.newItem = false;
            }
        },
        deleteItem(event) {
            if (!this.selectedItems || this.selectedItems.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un elemento de la lista.', life: 3000 });
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
                            this.deleteTransaccionesLote(item.id);
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
            if (!values.tipo.id) {
                errors.tipo = [{ message: 'El tipo es requerido.' }];
            }
            if (!values.concept) {
                errors.concept = [{ message: 'El concepto es requerido.' }];
            }
            if (!values.date) {
                errors.date = [{ message: 'La fecha es requerida.' }];
            }
            if (!values.ammount) {
                errors.ammount = [{ message: 'El monto es requerido.' }];
            }
            if (values.ammount <= 0) {
                errors.ammount = [{ message: 'El monto debe ser mayor a cero' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.concept = values.concept;
            this.initialValues.ammount = values.ammount;
            this.initialValues.date = values.date;
            this.initialValues.tipo = values.tipo;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues.date = this.formatDateShort(this.selectedDate);
                    this.initialValues.transactionType = this.selectedType;
                    this.initialValues = await this.createTransaccionesLote();
                    this.refreshTable();
                    this.toast.add({ severity: 'success', summary: 'Nueva transaccion agregada correctamente.', life: 3000 });
                } else {
                    this.initialValues.date = this.formatDateShort(this.selectedDate);
                    this.initialValues.transactionType = this.selectedType;
                    this.initialValues.transactionType = selectedType;
                    this.updateTransaccionesLote();
                    this.refreshTable();
                    this.toast.add({ severity: 'success', summary: 'Transaccion actualizada correctamente.', life: 3000 });
                }
            }
        },
        exportExcel() {
            if (this.items.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {

                const itemsExport = this.items.map(item => {
                    return {
                        Fecha: item.date,
                        Tipo: item.transactionType.tipo,
                        Tipo_de_Transaccion: item.transactionType.name,
                        Concepto: item.concept,
                        Cargo: item.transactionType.tipo === 'CARGO' ? item.ammount : '',
                        Abono: item.transactionType.tipo === 'ABONO' ? item.ammount : ''
                    };
                });
                const worksheet = XLSX.utils.json_to_sheet(itemsExport);
                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, 'Transacciones por Lote');

                // Genera el archivo binario
                const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
                const data = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

                // Descarga el archivo
                saveAs(data, 'Transacciones por Lote.xlsx');
            }
        }
    }, watch: {
        selectedCondominio(newValue, oldValue) {
            this.refreshTable();
            this.selectedItems = null;
            this.items = null;
        }
    },
    mounted() {
        this.refreshTable();
    },
    computed: {
        total() {
            let total = 0;
            if (this.items) {
                for (let item of this.items) {
                    total += item.ammount;
                }
            }
            return total;
        },
    }
}

</script>

<style scope>
.cargo {
    color: red;
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