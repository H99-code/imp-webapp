<script>
import PagesTopBar from "@/components/PagesTopBar.vue";

export default {
  components: {PagesTopBar},

  data() {

    return {
      components: PagesTopBar,
      selectedTheme: "darkmode",
      selectedLanguage: "englisch",
      themeSettings: ["Light Mode", "Dark Mode", "System"],
      languageSettings: ["Deutsch", "Englisch", "Französisch"],
    };
  },
  methods: {
    updateTheme() {
      console.log("Theme geändert zu:", this.selectedTheme);

    },
    updateLanguage() {
      console.log("Sprache geändert zu:", this.selectedLanguage);

    },
  },
};
</script>

<template>
  <PagesTopBar/>
  <v-container class="d-flex justify-center">
    <v-card width="400px" class="pa-4">
    <v-list>
      <v-select
          label="Change Theme"
          v-model="selectedTheme"
          :items="themeSettings"
          @update:modelValue="updateTheme"

      ></v-select>

      <v-select

          label="Change Language"
          v-model="selectedLanguage"
          :items="languageSettings"
          @update:modelValue="updateLanguage"

      ></v-select>
    </v-list>
  </v-card>
    </v-container>
</template>
