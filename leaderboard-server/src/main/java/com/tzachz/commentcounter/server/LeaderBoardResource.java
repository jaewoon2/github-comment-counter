package com.tzachz.commentcounter.server;

import com.google.common.collect.ImmutableList;
import com.tzachz.commentcounter.apifacade.jsonobjects.GHOrg;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: tzachz
 * Date: 10/08/13
 * Time: 13:57
 */
@Path("/leaderboard/{period}")
public class LeaderBoardResource {

    private final LeaderBoardStore store;
    private final GHOrg org;

    public LeaderBoardResource(LeaderBoardStore store, GHOrg org) {
        this.store = store;
        this.org = org;
    }

    private CommenterToRecordTransformer createTransformer() {
        return new CommenterToRecordTransformer(ImmutableList.of(
                new CapSizeCommentRenderer(),
                new CodeSnippetCommentRenderer(),
                new BoldCommentRenderer(),
                new EmojisCommentRenderer(store.getEmojiMap())
        ));
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public LeaderBoardView getLeaderBoard(@PathParam("period") String period) {
        return new LeaderBoardView(store.get(period), createTransformer(), org.getName(), org.getHtmlUrl(), store.isLoaded(period), period);
    }

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public LeaderBoardView getJson(@PathParam("period") String period) {
        return new LeaderBoardView(store.get(period),createTransformer(), org.getName(), org.getHtmlUrl(), store.isLoaded(period), period);
    }

}
