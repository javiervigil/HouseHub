<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div>
        <b style="color: var(--p-tabs-tab-active-color);">Seleccione el lote: </b>
        <Select v-model="selectedLote" :options="lotes" optionLabel="name" @change="refreshTable" style="width: 250px;"
            size="small" />
        <Button icon="pi pi-sync" variant="text" v-tooltip.top="'Actualizar lotes'" @click="refreshTable"
            style="margin-right: 20px;" />

        <Button icon="pi pi-plus" variant="outlined" label="Agregar abono" @click="addAbono"
            style="margin-right: 10px;" />
        <Button icon="pi pi-minus" variant="outlined" label="Agregar cargo" @click="addCargo" />
    </div>
    <br /><b style="color: var(--p-tabs-tab-active-color);">Transacciones </b>

    <div style="width: 70%;">
        <TableBar :showIcons="[0, 0, 1, 1, 1]" @deleteItem="deleteItem" @refreshTable="refreshTable"
            @exportExcel="exportExcel" />

        <DataTable v-model:selection="selectedItems" :value="itemsCA" stripedRows size="small"
            :metaKeySelection="metaKey" dataKey="id" :sortOrder="1" selectionMode="multiple">
            <template #empty> No existen datos para esta tabla. </template>
            <template #loading> Cargando informacion.... </template>

            <Column selectionMode="multiple" style="width: 1%;"></Column>
            <Column field="date" header="Fecha" style="width: 20%;">
                <template #body="slotProps">
                    {{ slotProps.data.date }}
                </template>
            </Column>
            <Column field="transactionType.name" header="Tipo de transaccion" style="width: 20%;"></Column>
            <Column field="concept" header="Concepto" style="width: 20%;"></Column>
            <Column header="Cargo" style="width: 15%;">
                <template #body="slotProps">
                    <div v-if="slotProps.data.transactionType.tipo === 'CARGO'" class="cargo">
                        {{ formatCurrency(slotProps.data.ammount) }}
                    </div>
                </template>
            </Column>
            <Column header="Abono" style="width: 15%;">
                <template #body="slotProps">
                    <div v-if="slotProps.data.transactionType.tipo === 'ABONO'">
                        {{ formatCurrency(slotProps.data.ammount) }}
                    </div>
                </template>
            </Column>
            <ColumnGroup type="footer">
                <Row>
                    <Column footer="Totales:" :colspan="4" footerStyle="text-align:right" />
                    <Column>
                        <template #footer="slotProps">
                            <div><b class="cargo">{{ formatCurrency(cargoTotal) }}</b></div>
                        </template>
                    </Column>
                    <Column :footer="formatCurrency(abonoTotal)" />
                </Row>
                <Row>
                    <Column footer="Diferencia:" :colspan="4" footerStyle="text-align:right" />
                    <Column>
                        <template #footer="slotProps">
                            <div v-if="abonoTotal - cargoTotal < 0"><b class="cargo">{{ formatCurrency(abonoTotal -
                                cargoTotal) }} (saldo en contra)</b></div>
                        </template>
                    </Column>
                    <Column>
                        <template #footer="slotProps">
                            <div v-if="abonoTotal - cargoTotal > 0"><b>{{ formatCurrency(abonoTotal - cargoTotal) }}
                                    (saldo a favor)</b>
                            </div>
                            <div v-if="abonoTotal - cargoTotal === 0">$ 0.00</div>
                        </template>
                    </Column>

                </Row>
            </ColumnGroup>

        </DataTable>

    </div>


    <!-- Add / Edit item -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">
                <div class="flex flex-col gap-1">
                    <Select name="tipo" :options="items" optionLabel="name" placeholder="Tipo" fluid
                        style=" margin-bottom: 10px;  margin-top: 20px;" v-model="selectedType" size="small">
                    </Select>
                    <Message v-if="$form.tipo?.invalid" severity="error" size="small" variant="simple">{{
                        $form.tipo.error.message }}</Message>
                </div>

                <div class="flex flex-col gap-1">
                    <DatePicker name="date" showIcon fluid iconDisplay="input" dateFormat="dd M yy"
                        v-model="selectedDate" size="small" showButtonBar />
                    <Message v-if="$form.date?.invalid" severity="error" size="small" variant="simple">{{
                        $form.date.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="concept" type="text" placeholder="Concepto" fluid size="small" />
                    <Message v-if="$form.concept?.invalid" severity="error" size="small" variant="simple">{{
                        $form.concept.error.message }}</Message>
                </div>

                <div class="flex flex-col gap-1">
                    <InputText name="ammount" type="number" placeholder="Monto" fluid size="small" />
                    <Message v-if="$form.ammount?.invalid" severity="error" size="small" variant="simple">{{
                        $form.ammount.error.message }}</Message>
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
import { Button, Column, ColumnGroup, ConfirmPopup, DataTable, DatePicker, Dialog, InputText, Message, Row, Select, Toast } from 'primevue';
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
        DataTable, Column, ColumnGroup, Row, TableBar, Dialog, InputText, Button, Form, Select, DatePicker, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            lotes: ref([]),

            selectedLote: null,
            itemsCA: ref([]),
            selectedType: ref(),

            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            selectedDate: null,
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
                itemsCA: [],
                loteId: null
            }
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                if (this.selectedLote) {
                    this.itemsCA = (await apiService.getTransaccionesByLote(this.selectedLote.id)).data;
                }
            } catch (error) {
                console.error("Failed to fetch TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async refreshLotes() {
            try {
                if (this.selectedCondominio) {
                    this.lotes = (await apiService.getLotesByCondominio(this.selectedCondominio.id)).data;
                }
            } catch (error) {
                console.error("Failed to fetch TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async updateTransaccionesLote() {
            try {
                return (await apiService.updateTransaccionesLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async createTransaccionesLote() {
            try {
                return (await apiService.createTransaccionesLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async deleteTransaccionesLote(id) {
            try {
                await apiService.deleteTransaccionesLote(id);
                this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                this.selectedItems = null;
                await this.refreshTable();
            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async getAbonosList() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getTransaccionesLoteTypesByCondominio(this.selectedCondominio.id, 1)).data;
                }
            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
            }
        },
        async getCargosList() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getTransaccionesLoteTypesByCondominio(this.selectedCondominio.id, 2)).data;
                }
            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
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
        async addAbono() {
             if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un abono.', life: 3000 });
                return;
            }
            if (this.selectedLote) {
                this.titulo = 'Agregar abono';
                this.initialValues.id = null;
                this.initialValues.tipo = 1;
                this.initialValues.concept = '';
                this.initialValues.ammount = '';
                this.selectedDate = null;
                this.initialValues.date = this.formatDateShort(new Date());
                this.initialValues.type = 'ABONO';
                this.initialValues.loteId = this.selectedLote.id;
                this.initialValues.itemsCA = await this.getAbonosList();
                this.selectedType = null;
                this.showAdd = true;
                this.newItem = true;
            } else {
                this.toast.add({ severity: 'warn', summary: 'Seleccione un lote de la lista.', life: 3000 });
            }
        },
        async addCargo() {
             if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un cargo.', life: 3000 });
                return;
            }
            if (this.selectedLote) {
                this.titulo = 'Agregar cargo';
                this.initialValues.id = null;
                this.initialValues.tipo = 2;
                this.initialValues.concept = '';
                this.initialValues.ammount = '';
                this.selectedDate = null;
                this.initialValues.date = this.formatDateShort(new Date());
                this.initialValues.type = 'CARGO';
                this.initialValues.loteId = this.selectedLote.id;
                this.initialValues.itemsCA = await this.getCargosList();
                this.selectedType = null;
                this.showAdd = true;
                this.newItem = true;
            } else {
                this.toast.add({ severity: 'warn', summary: 'Seleccione un lote de la lista.', life: 3000 });
            }
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
                    //this.items = [...this.items, this.initialValues];
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
            if (this.itemsCA.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {

                const itemsExport = this.itemsCA.map(item => {
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
            this.refreshLotes();
            this.selectedItems = null;
            this.itemsCA = null;
        }
    },
    mounted() {
        this.refreshLotes();
    },
    computed: {
        cargoTotal() {
            let total = 0;
            if (this.itemsCA) {
                for (let item of this.itemsCA) {
                    if (item.transactionType.tipo === 'CARGO') {
                        total += item.ammount;
                    }
                }
            }
            return total;
        },
        abonoTotal() {
            let total = 0;
            if (this.itemsCA) {
                for (let item of this.itemsCA) {
                    if (item.transactionType.tipo === 'ABONO') {
                        total += item.ammount;
                    }
                }
            }
            return total;
        }
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