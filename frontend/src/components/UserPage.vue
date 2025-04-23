<script>
import PagesTopBar from "@/components/PagesTopBar.vue";


export default {
  components: { PagesTopBar },
  data() {
    return {
      user: { personalInfo: { firstName: "", lastName: "", email: "" } },
      editMode: false,
    };
  },
  created() {
    const userId = localStorage.getItem('userId');
    if (userId) {
        this.user = "foundUser";
      } if(userId) {
        this.$router.push('/login');

    } else {
      this.$router.push('/login');
    }
  },
  methods: {
    toggleEdit() {
      this.editMode = !this.editMode;
    },
    saveChanges() {
      this.editMode = false;
      console.log('Changes saved', this.user);
    },
  },
};
</script>



<template>
  <PagesTopBar />

  <v-container class="d-flex justify-center">
    <v-card width="400px" class="pa-4">
      <v-list>
        <v-list-item>
          <v-text-field v-model="user.personalInfo.firstName" :disabled="!editMode" label="Name"></v-text-field>
          <v-text-field v-model="user.personalInfo.lastName" :disabled="!editMode" label="Nachname"></v-text-field>
          <v-text-field v-model="user.email" :disabled="!editMode" label="Email"></v-text-field>
        </v-list-item>
      </v-list>

      <v-row class="mt-2">
        <v-col>
          <v-btn color="primary" @click="toggleEdit">
            {{ editMode ? "Cancel" : "Edit" }}
          </v-btn>
        </v-col>
        <v-col>
          <v-btn color="success" :disabled="!editMode" @click="saveChanges">
            Save
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

