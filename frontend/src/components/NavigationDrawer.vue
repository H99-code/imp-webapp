<script>
export default {
  data() {
    return {
      drawer: true,
      menuItems: [
        { title: "Profile", icon: "mdi-account-circle", route: "/user" },
        { title: "Todo", icon: "mdi-format-list-bulleted", route: "/todo" },
        { title: "Links", icon: "mdi-link", route: "/links" },
        { title: "Settings", icon: "mdi-cog", route: "/settings" },
      ],
    };
  },
  methods: {
    navigate(route) {
      this.$router.push(route);
    },
    logout() {
      this.$router.push("/");
    },
  },
};
</script>

<template>
  <v-navigation-drawer v-model="drawer" app class="rounded-lg" width="250">
    <v-list density="compact">
      <v-list-item
          v-for="(item, index) in menuItems"
          :key="index"
          :prepend-icon="item.icon"
          @click="navigate(item.route)"
          variant="tonal"
      >
        <v-list-item-title>{{ item.title }}</v-list-item-title>
      </v-list-item>
    </v-list>

    <v-divider class="my-3"></v-divider>

    <v-col class="d-flex justify-center">
      <v-btn color="yellow-darken-2" rounded="lg" @click="logout">
        Log out
      </v-btn>
    </v-col>
  </v-navigation-drawer>
</template>
