<template>
  <v-app-bar image="https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExbTB0aGdqMmhvMTFyaHc0NzF0anNnYWQxbGl6a2UzNngyMDFkNWw4MSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/p4sfWzAfXm04LkNFsf/giphy.gif">
    <v-app-bar-nav-icon @click="$emit('toggle-drawer')"></v-app-bar-nav-icon>
    <v-toolbar-title>Web</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn icon to="/">
      <v-icon>mdi-export</v-icon>
    </v-btn>
  </v-app-bar>
</template>