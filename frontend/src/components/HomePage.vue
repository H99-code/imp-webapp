<template>
  <div>
    <TopBar @toggle-drawer="drawer = !drawer" />
    <NavigationDrawer v-model="drawer"></NavigationDrawer>
    <!--<WelcomeDialog :dialog="openWelcomeDialog"></WelcomeDialog> -->


    <v-container>
      <v-row>
        <v-col cols="8">
          <v-card class="elevation-13">
            <v-tabs v-model="activeTab">
              <v-tab v-for="user in users" :key="user.uuid">
                {{ user.username }}
              </v-tab>
            </v-tabs>
            <v-table>
              <thead>
              <tr>
                <th class="text-left">Item</th>
                <th class="text-left">Price</th>
                <th class="text-left">Link</th>
                <th class="text-left">Type</th>
                <th class="text-left">Status</th>
                <th class="text-left">Select</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in filteredItems" :key="index">
                <td>{{ item.name }}</td>
                <td>{{ item.price}}</td>
                <td><a :href="item.link" target="_blank">{{ item.link }}</a></td>
                <td>{{ item.type }}</td>
                <td><v-chip>{{ item.status }}</v-chip></td>
                <td>
                  <v-checkbox v-model="selectedItems" :value="index"></v-checkbox>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card>
        </v-col>


        <v-col cols="4">
          <v-card class="pa-4" elevation="13">
            <v-card-title>Actions</v-card-title>
            <v-card-text>
              <v-btn color="orange" block class="mb-2" @click="openAddItemDialog = true">
                ADD ITEM
              </v-btn>
              <v-btn color="red" block class="mb-2" @click="deleteSelectedItems">
                DELETE SELECTED
              </v-btn>
              <v-btn color="green" block class="mb-2" @click="buySelectedItems">
                BUY SELECTED
              </v-btn>
            </v-card-text>
          </v-card>
          <v-col cols="6"></v-col>
          <v-card class="pa-4">
            <ChatView ref="chatComponent" />
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <v-dialog v-model="openAddItemDialog" max-width="500px">
      <v-card elevation="12" class="pa-4">
        <v-card-title class="text-center">Add Item</v-card-title>
        <v-card-text>
          <v-text-field v-model="newItem.name" label="Item" :rules="itemRules"></v-text-field>
          <v-text-field v-model="newItem.link" label="Link" :rules="linkRules"></v-text-field>

          <v-text-field
              v-model="newItem.price"
              label="Price"

          >

          </v-text-field>

          <v-autocomplete
              v-model="newItem.type"
              clearable
              label="Type"
              :items="['Type 1', 'Type 2']"
              :rules="typeRules"
          ></v-autocomplete>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn color="red" @click="openAddItemDialog = false">Cancel</v-btn>
          <v-btn color="green" @click="addItem">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ChatView from "@/components/ChatView.vue";
import TopBar from "@/components/TopBar.vue";
import NavigationDrawer from "@/components/NavigationDrawer.vue";
//import WelcomeDialog from "@/components/WelcomeDialog.vue";


export default {
  data() {
    return {
      activeTab: 0,
      openAddItemDialog: false,
      newItem: { name: "", link: "", type: "", price: "" }
      ,

      selectedItems: [],
      users: [],
      openWelcomeDialog: true,
      drawer: true,
      itemRules: [v => !!v || "Item Name is required"],
      typeRules: [v => !!v || "Item Type is required"],
      linkRules: [v => !!v || "Item Link is required"],
    };
  },
  components: {
    //WelcomeDialog,
    NavigationDrawer,
    ChatView,
    TopBar,
  },

  computed: {
    filteredItems() {
      return this.currentUser?.items ?? [];
    },
    currentUser() {
      return this.users[this.activeTab] ?? { items: [] };
    }
  },

  watch: {
    activeTab() {
      this.fetchItem();
    }
  },
  async mounted() {
    await this.fetchUsers();
    if (this.users.length > 0) {
      this.activeTab = 0;
      await this.fetchItem();
    }
  },

  methods: {
    async fetchUsers() {
      try {
        const response = await fetch("http://localhost:8080/users");
        if (!response.ok) {
          throw new Error("Fehler beim Laden der Benutzer");
        }
        const data = await response.json();
        this.users = data._embedded.users.map(users => ({
          ...users,
          uuid: users._links.self.href.split('/').pop()
        }));
      } catch (error) {
        console.error("Fehler beim Abrufen der Benutzerdaten:", error);
      }
    },
    async fetchItem() {
      try {
        const currentUser = this.users[this.activeTab];

        if (!currentUser || !currentUser._links || !currentUser._links.items) {
          console.warn("Kein Benutzer ausgewählt oder keine Items-URL vorhanden");
          return;
        }

        const response = await fetch(currentUser._links.items.href);

        console.log("Status der Antwort:", response.status);

        if (!response.ok) throw new Error("Fehler beim Abrufen der Items");

        const data = await response.json();
        console.log("Antwortdaten:", data);

        if (!data._embedded || !data._embedded.items) {
          console.warn("Keine Items vorhanden");
          this.users[this.activeTab].items = [];
          return;
        }

        this.users[this.activeTab].items = data._embedded.items;
      } catch (error) {
        console.error("Fehler beim Abrufen der Items:", error);
        alert("Es gab ein Problem beim Laden der Items. Bitte versuche es später noch einmal.");
      }
    },









    async addItem() {
      try {
        console.log("Aktiver Tab:", this.activeTab);

        const currentUser = this.users[this.activeTab];

        console.log("Aktueller Benutzer:", currentUser);

        if (!currentUser || !currentUser._links || !currentUser._links.items) {
          console.warn("Kein Benutzer ausgewählt oder keine Items-URL vorhanden");
          return;
        }

        const userId = currentUser.uuid;
        if (!userId) {
          console.warn("Keine gültige User ID gefunden");
          return;
        }

        const response = await fetch(`http://localhost:8080/api/${userId}/items`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            name: this.newItem.name,
            link: this.newItem.link,
            type: this.newItem.type,
            price: this.newItem.price
          })
        });

        console.log("Status der Antwort:", response.status);

        if (!response.ok) throw new Error("Fehler beim Hinzufügen des Items");

        const data = await response.json();
        console.log("Antwortdaten:", data);

        if (data.success && data.item) {
          currentUser.items.push(data.item);


        }

        this.resetNewItem();


        this.openAddItemDialog = false;

      } catch (error) {
        console.error("Fehler beim Hinzufügen des Items:", error);
        alert("Es gab ein Problem beim Hinzufügen des Items. Bitte versuche es später noch einmal.");
      } finally {

        this.openAddItemDialog = false;
      }
    }


    ,



    deleteSelectedItems() {
      const currentUser = this.users[this.activeTab];
      currentUser.items = currentUser.items.filter((_, index) => !this.selectedItems.includes(index));
      this.selectedItems = [];
    },

    buySelectedItems() {
      const currentUser = this.users[this.activeTab];
      currentUser.items.forEach((item, index) => {
        if (this.selectedItems.includes(index)) {
          item.status = "Bought";
        }
      });
      this.selectedItems = [];
    },

    resetNewItem() {
      this.newItem = {name: "", price: [], link: "", type: "", status: ""};
    }
  }
}
</script>