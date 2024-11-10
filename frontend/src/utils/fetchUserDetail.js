import axios from "./axiosInstance";
import { useUserStore } from "@/stores/store";

export function getUserDetails(){
    const userStore = useUserStore()
    axios.get(`clanuser/api/users/${userStore.user.id}/overall`).then((response)=>{
        if(response.status == 200){
          const data = response.data;
          let userInfo = data.clanUser.user;
          userInfo['id'] = userInfo.userId;
          userInfo['clan'] = data.clanUser.clan;
          userInfo['clanRole'] = data.clanUser.isClanLeader ? 'ROLE_ADMIN' : 'ROLE_PLAYER';
          userInfo['eloRank'] = data.eloRank;
          userInfo['stats'] = data.stats;
          userInfo['rank'] = data.eloRank.rankThreshold.rank;
          userInfo['eloLowerlimit'] = data.eloRank.rankThreshold.minRating;
          userInfo['currentElo'] = data.eloRank.meanSkillEstimate;
          userInfo['eloUpperlimit'] = data.eloRank.rankThreshold.maxRating;
          userStore.setUser(userInfo);
          console.log(userStore.user)
        }
      })
}
