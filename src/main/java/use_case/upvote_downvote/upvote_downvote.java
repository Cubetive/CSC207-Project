// package use_case.upvoting_downvoting

// upvote() and downvote() for Post class
//public void upvote(Post post){
//    post.votes[0] += 1;
//}
//
//public void downvote(Post post){
//    post.votes[1] += 1;
//}

// Pseudo code for handling comments in order of votes
// For ordering the replies (put in OriginalPost class)
//for (ReplyPost reply: this.replies) {
//if (the downvotes of reply > downvotes of the one reply below) {
//    swap the position of the two replies
//else if (the upvotes of this reply < upvotes of the one reply below) {
//    swap the position of the two replies
//        }
//     }