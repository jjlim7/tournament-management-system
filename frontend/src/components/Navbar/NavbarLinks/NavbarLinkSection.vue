<template>
  <ul class="navbar-nav text-center">
    <NavbarlinkCard
      v-for="eachLink in linksToPages"
      :key="eachLink.linkName"
      :eachLink="eachLink"
    />
  </ul>
</template>

<script>
import NavbarlinkCard from "./NavbarlinkCard.vue";

export default {
  name: "NavbarLinkSection1",
  components: {
    NavbarlinkCard,
  },

  data() {
    return {
      linksToPages: [
        {
          linkName: "BATTLE ROYALE",
          routeTo: "/battleroyale",
        },
        {
          linkName: "CLAN WAR",
          routeTo: "/clanwar",
        },
        {
          linkName: "BOOKING",
          routeTo: "/booking",
        },
        {
          linkName: "CLAN",
          routeTo: "/Clan",
        },
        {
          linkName: "LEADERBOARD",
          routeTo: "/leaderboard",
        },
      ],
    };
  },
};
</script>