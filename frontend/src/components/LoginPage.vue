<template>
  <v-container fill-height>
    <v-row class="d-flex justify-center" :style="{ marginTop: '10vh' }">
      <v-col xs="12" sm="8" md="6" lg="4" xl="3" class="d-flex justify-center">
        <v-card elevation="22" class="gif-background" width="100%" color="red">
          <v-form ref="loginForm">
            <v-col>
              <v-text-field
                  v-model="username"
                  label="Username"
                  :rules="usernameRules"
                  variant="solo-filled"
              ></v-text-field>
            </v-col>

            <v-col>
              <v-text-field
                  v-model="password"
                  label="Password"
                  :rules="passwordRules"
                  variant="solo-filled"
                  type="password"
              ></v-text-field>
            </v-col>

            <v-alert v-if="loginError" type="error" class="mx-4">
              {{ errorMessage }}
            </v-alert>

            <v-row>
              <v-col>
                <v-btn color="green" @click="goToRegister">Register</v-btn>
              </v-col>
              <v-col class="d-flex justify-end">
                <v-btn :disabled="!username || !password" color="blue" @click="handleLogin">
                  Login
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>


export default {
  data() {
    return {
      username: '',
      password: '',
      loginError: false,
      errorMessage: '',
      usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length >= 3) || 'Username muss länger als 3 Zeichen sein'
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 3) || 'Password muss länger als 3 Zeichen sein'
      ]
    };
  },
  methods: {
    goToRegister() {
      this.$router.push("/register");
    },
    async handleLogin() {
      this.loginError = false;
      this.errorMessage = '';

      const isFormValid = await this.$refs.loginForm.validate();
      if (!isFormValid) {
        this.errorMessage = "Bitte alle Felder korrekt ausfüllen!";
        this.loginError = true;
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/api/login", {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({
            username: this.username,
            password: this.password
          })
        });


        const result = await response.json();

        if (!response.ok) {
          throw new Error(result.message || "Login fehlgeschlagen");
        }


        alert("Login erfolgreich!");
        this.$router.push('/home');
      } catch (error) {
        this.errorMessage = error.message;
        this.loginError = true;
      }
    }
  }
}
</script>

<style scoped>
.gif-background {
  background-image: url('https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExbTB0aGdqMmhvMTFyaHc0NzF0anNnYWQxbGl6a2UzNngyMDFkNWw4MSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/p4sfWzAfXm04LkNFsf/giphy.gif');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 100%;
  padding: 20px;
}
</style>
