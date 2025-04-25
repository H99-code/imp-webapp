<template>
  <div>
    <TopBar @toggle-drawer="drawer = !drawer" />
    <NavigationDrawer v-model="drawer"></NavigationDrawer>

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
                <td>{{ item.price }}</td>
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

          <v-text-field v-model="newItem.price" label="Price"></v-text-field>

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

export default {
  data() {
    return {
      activeTab: 0,
      openAddItemDialog: false,
      newItem: { name: "", link: "", type: "", price: "" },
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
        const response = await fetch("https://5ade-2a01-41e3-2320-2500-41b4-734d-a746-9d87.ngrok-free.app/api/users");

        // Wenn die Antwort nicht OK ist, werfe einen Fehler
        if (!response.ok) {
          throw new Error("Fehler beim Laden der Benutzer");
        }

        // Logge die Antwort als Text, falls etwas im JSON-Parsing schiefgeht
        const responseText = await response.text();
        console.log("Antwort Text:", responseText);  // Logge die gesamte Antwort als Text

        // Überprüfe, ob die Antwort tatsächlich im JSON-Format ist
        if (!response.headers.get('content-type').includes('application/json')) {
          throw new Error('Die Antwort war nicht im JSON-Format');
        }

        // Parst die Antwort als JSON
        const data = JSON.parse(responseText);

        // Jetzt kannst du sicher auf die Daten zugreifen
        this.users = data.users.map(user => ({
          ...user,
          uuid: user.id
        }));
      } catch (error) {
        console.error("Fehler beim Abrufen der Benutzerdaten:", error);
        this.errorMessage = error.message;
      }
    },

    async fetchItem() {
      try {
        const currentUser = this.users[this.activeTab];

        if (!currentUser || !currentUser.items) {
          console.warn("Kein Benutzer ausgewählt oder keine Items vorhanden");
          return;
        }

        const response = await fetch(currentUser.items.href);
        if (!response.ok) throw new Error("Fehler beim Abrufen der Items");

        const data = await response.json();
        if (!data.items) {
          console.warn("Keine Items vorhanden");
          this.users[this.activeTab].items = [];
          return;
        }

        this.users[this.activeTab].items = data.items;
      } catch (error) {
        console.error("Fehler beim Abrufen der Items:", error);
        alert("Es gab ein Problem beim Laden der Items. Bitte versuche es später noch einmal.");
      }
    },

    async addItem() {
      try {
        const currentUser = this.users[this.activeTab];
        if (!currentUser) {
          console.warn("Kein Benutzer ausgewählt");
          return;
        }

        const response = await fetch(`https://5ade-2a01-41e3-2320-2500-41b4-734d-a746-9d87.ngrok-free.app/api/${currentUser.uuid}/items`, {
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

        if (!response.ok) throw new Error("Fehler beim Hinzufügen des Items");

        const data = await response.json();
        if (data.success && data.item) {
          currentUser.items.push(data.item);
        }

        this.resetNewItem();
        this.openAddItemDialog = false;
      } catch (error) {
        console.error("Fehler beim Hinzufügen des Items:", error);
        alert("Es gab ein Problem beim Hinzufügen des Items. Bitte versuche es später noch einmal.");
      }
    },

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
      this.newItem = { name: "", price: "", link: "", type: "", status: "" };
    }
  }
};
</script>
