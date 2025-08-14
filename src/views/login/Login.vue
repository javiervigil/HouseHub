<template>
  <div class="main-content-login">
    <Toast />
    <div class="login-container">
      <div class="login-image">
        <img src="@/assets/img8.jpg" alt="Residential Pro">
      </div>
      <div class="login-form">
        <h1>House Hub</h1>
        <h3>Iniciar Sesión</h3>
        <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
          :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
          class="flex flex-col gap-4 w-full sm:w-56">

          <div class="flex flex-col gap-1">
            <Select v-model="selectedType" :options="userTypes" optionLabel="name"  fluid />
          </div>

          <div class="flex flex-col gap-1">
            <InputText name="email" type="email" placeholder="email" fluid />
            <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">{{
              $form.email.error.message }}</Message>
          </div>
          <div class="flex flex-col gap-1">
            <InputText name="password" type="password" placeholder="Password" fluid />
            <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">{{
              $form.password.error.message }}</Message>
          </div>

          <Message v-if="loginErr" severity="error" size="small" variant="simple">{{
            loginErr }}</Message>
          <Button type="submit" severity="secondary" label="Submit" style="margin-top: 20px;" />
        </Form>
        {{ selectedType }}
      </div>
    </div>
  </div>
</template>

<script>
import { Button, InputText, Message, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { ref } from 'vue';
import { useAuthStore } from '@/service/auth';
import { useRouter } from 'vue-router';
import { useToast } from 'primevue/usetoast';
import apiService from '@/service/apiService';

export default {
  components: {
    Button, Form, InputText, Message, Toast, Select
  },
  data() {
    return {
      toast: useToast(),
      authStore: useAuthStore(),
      router: useRouter(),
      token: ref(''),
      loginErr: ref(''),
      initialValues: ref({
        email: 'jj@jj.com',
        password: 'jj'
      }),
      selectedType: { name: 'Usuario', id: '1' },
      userTypes: [
        { name: 'Usuario', id: '1' },
        { name: 'Administrador', id: '2' }
      ]
    }
  },
  methods: {
    async handleLogin() {
      try {
        let response = null;
        console.log("Selected type:", this.selectedType.id);
        if(this.selectedType.id === 1){//admin login     
          console.log("Admin login");     
          response = await apiService.loginAdmin(this.initialValues);
        }else{//normal login
          console.log("User login");
          response = await apiService.loginUser(this.initialValues);
        }
        console.log("Login response:", response);
        this.userData = response.data;
        if (this.userData) {
          this.toast.add({ severity: 'info', summary: 'Dato login.' + this.userData.id, life: 3000 });

          this.userData = { email: 'Usuario', passwrd: 'user@example.com' };
          this.token = 'ejemplo-de-token-jwt';
          this.authStore.login(this.userData, this.token);
          return true;
        } else {
          return false;
        }
      } catch (error) {
        console.error("Failed to login:", error);
      }
    },

    resolver({ values }) {
      const errors = {};
      if (!values.email) {
        errors.email = [{ message: 'Email is required.' }];
      }
      if (!values.password) {
        errors.password = [{ message: 'Password is required.' }];
      }
      this.initialValues.email = values.email;
      this.initialValues.password = values.password;
      return {
        errors
      };
    },
    onFormSubmit({ valid }) {
      if (valid) {
        this.handleLogin();
        this.toast.add({ severity: 'info', summary: 'this.authStore.isLoggedIn.' + this.authStore.isLoggedIn, life: 3000 });
        if (this.authStore.isLoggedIn) {
          this.toast.add({ severity: 'success', summary: 'Login successfully.', life: 3000 });
          //this.$router.push({ name: 'home' });
        } else {
          this.loginErr = 'User / password incorrect.';
          this.toast.add({ severity: 'error', summary: 'User / password incorrect.', life: 3000 });
        }
      }
    }
  }
}
</script>

<style scoped>
input {
  margin-bottom: 10px;
  margin-top: 10px;
}

.login-container {
  display: flex;
  overflow: hidden;
  max-width: 900px;
  align-content: center;
  align-items: center;
  align-self: center;
  border-radius: 10px;
  background-color: white;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
  padding: 50px;
}

.login-image {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 20px;
}

.login-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.login-form {
  flex: 1;
  /* Le da al formulario el mismo peso flexible que la imagen */
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.main-content-login {
  align-content: center;
  align-items: center;
  text-align: center;
  align-self: center;
  padding: 50px;
  overflow-y: auto;
  justify-content: space-between;
  display: flex;
  flex-direction: column;
}
</style>
