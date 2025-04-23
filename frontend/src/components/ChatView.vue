<template>
  <v-container class="chat-container">
    <v-card elevation="14" height="400px">
      <v-card-title class="chat-header">{{ title }}</v-card-title>
      <v-card-text class="chat-messages" ref="chatMessages">
        <div
            v-for="(msg, index) in messages"
            :key="index"
            :class="{'my-message': msg.isMine, 'received-message': !msg.isMine}"
            ref="messageItems"
        >
          <span>{{ msg.text }}</span>
        </div>
      </v-card-text>
      <v-card-actions class="chat-input">
        <v-text-field
            variant="outlined"
            v-model="newMessage"
            label="Nachricht..."
            outlined
            dense
            @keyup.enter="sendMessage"
            class="flex-grow-1"
        />
        <v-btn variant="text" color="primary" @click="sendMessage">Senden</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "Chat",
    },
    initialMessages: {
      type: Array,
      default: () => [{ text: "Willkommen im Chat!", isMine: false }],
    },
    autoReplies: {
      type: Object,
      default: () => ({
        hello: "Hallo!",
        help: "Wie kann ich helfen?",
      }),
    },
    reminder: {
      type: Object,
      default: () => ({
        date: null,
        message: "",
      }),
    },
  },
  data() {
    return {
      newMessage: "",
      messages: [],
    };
  },
  created() {
    this.messages = [...this.initialMessages];
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() !== "") {
        const userMessage = this.newMessage.trim();
        this.messages.push({ text: userMessage, isMine: true });
        this.newMessage = "";

        this.$nextTick(() => {
          this.scrollToBottom();
        });

        const replyKey = Object.keys(this.autoReplies).find(key =>
            userMessage.toLowerCase().includes(key.toLowerCase())
        );
        if (replyKey) {
          this.messages.push({ text: this.autoReplies[replyKey], isMine: false });
        }

        if (this.reminder.date) {
          const reminderDate = new Date(this.reminder.date);
          const now = new Date();
          if (
              reminderDate.getFullYear() === now.getFullYear() &&
              reminderDate.getMonth() === now.getMonth() &&
              reminderDate.getDate() === now.getDate() &&
              reminderDate.getHours() === now.getHours() &&
              reminderDate.getMinutes() === now.getMinutes()
          ) {
            this.messages.push({ text: this.reminder.message, isMine: false });
          }
        }
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatBox = this.$refs.chatMessages;
        const messages = this.$refs.messageItems;
        if (chatBox && messages && messages.length) {
          messages[messages.length - 1].scrollIntoView({ behavior: "smooth" });
        }
      });
    },
  },
};
</script>

<style scoped>
.chat-header {
  background-color: rebeccapurple;
  color: white;
  padding: 10px;
  font-weight: bold;
  text-align: center;
}

.chat-messages {
  height: 250px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.my-message,
.received-message {
  max-width: 70%;
  padding: 8px 12px;
  margin: 5px;
  border-radius: 18px;
  word-wrap: break-word;
}

.my-message {
  background-color: #2582d3;
  color: white;
  align-self: flex-end;
  margin-left: auto;
}

.received-message {
  background-color: #bf4343;
  align-self: flex-start;
}
</style>
