<template>
<v-dialog
    v-model="openWelcomeDialog"
    max-width="500px"
    class="custom-dialog-overlay"

>
<v-card elevation="14">
  <v-card-title>Welcome</v-card-title>
  <v-card-text>This Is a welcome text about this page. This Page is For this reason. How to Use This Page</v-card-text>
  <v-col>
    <v-btn block color="purple" @click="openWelcomeDialog = false">Start</v-btn>
  </v-col>
</v-card>
</v-dialog>
</template>
<script >

export default {
  data() {
    return {


      openWelcomeDialog: true,

      drawer: true,

      itemRules: [v => !!v || "Item Name is required"],
      typeRules: [v => !!v || "Item Type is required"],
      linkRules: [v => !!v || "Item Link is required"],
      user1: [
        {item: "Item 1", price: [10, 30], link: "https://example.com", type: "Type 1", status: "Active"}
      ],
      user2: [
        {item: "Item 2", price: [40, 60], link: "https://example.com", type: "Type 2", status: "Pending"}
      ]
    };
  },
  components: {

  },
  computed: {

  },
  methods: {


  }
}

</script>
