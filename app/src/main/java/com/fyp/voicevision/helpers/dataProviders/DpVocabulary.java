package com.fyp.voicevision.helpers.dataProviders;

import com.fyp.voicevision.helpers.models.VocabularyEachItem;
import com.fyp.voicevision.helpers.models.VocabularyItem;

import java.util.ArrayList;
import java.util.List;

public class DpVocabulary {

    public List<VocabularyItem> getVocabularyList() {
        List<VocabularyItem> arrayList = new ArrayList<>();
        arrayList.add(new VocabularyItem(0, "Concise and General Sentences"));
        arrayList.add(new VocabularyItem(1, "Giving General Good Wishes"));
        arrayList.add(new VocabularyItem(2, "Responding to General Good Wishes"));
        arrayList.add(new VocabularyItem(3, "Inviting someone to.."));
        arrayList.add(new VocabularyItem(4, "Accepting the Invitation"));
        arrayList.add(new VocabularyItem(5, "Declining the Invitation"));
        /*arrayList.add(new VocabularyItem(6, "Complimenting"));
        arrayList.add(new VocabularyItem(7, "Congratulating"));
        arrayList.add(new VocabularyItem(8, "Responding to Compliment and Congratulations"));
        arrayList.add(new VocabularyItem(9, "Making Apology"));
        arrayList.add(new VocabularyItem(10, "Accepting the Apology"));
        arrayList.add(new VocabularyItem(11, "Showing Sympathy"));
        arrayList.add(new VocabularyItem(12, "Leaving Someone Politely for a short time."));
        arrayList.add(new VocabularyItem(13, "Giving Reasons"));
        arrayList.add(new VocabularyItem(14, "Dp you Agree?"));
        arrayList.add(new VocabularyItem(15, "I Agree"));
        arrayList.add(new VocabularyItem(16, "Disagreeing"));
        arrayList.add(new VocabularyItem(17, "Party Agreeing"));
        arrayList.add(new VocabularyItem(18, "Giving Good Wishes on Special Occasions"));
        arrayList.add(new VocabularyItem(19, "Responding to Good Wishes on Special Occasions"));
        arrayList.add(new VocabularyItem(20, "Accepting an Offer"));
        arrayList.add(new VocabularyItem(21, "Declining an Offer"));
        arrayList.add(new VocabularyItem(22, "Taking Information"));
        arrayList.add(new VocabularyItem(23, "Correcting Someone"));
        arrayList.add(new VocabularyItem(24, "Talking About What Might Happen"));
        arrayList.add(new VocabularyItem(25, "Reminding"));*/
        return arrayList;
    }

    public List<VocabularyEachItem> getEachVocabularyList(int vid) {
        List<VocabularyEachItem> arrayList = new ArrayList<>();

        switch (vid) {
            case 0: {
                arrayList.add(new VocabularyEachItem(0, vid, "Look here.", "یہاں دیکھو"));
                arrayList.add(new VocabularyEachItem(1, vid, "Wait Outside", "باہر انتظار کرو "));
                arrayList.add(new VocabularyEachItem(2, vid, "Drive Slowly", "اہستہ چلائیں"));
                arrayList.add(new VocabularyEachItem(3, vid, "Look ahead", "آگے دیکھو"));
                arrayList.add(new VocabularyEachItem(4, vid, "Get Off", "دفع ہوجاؤ"));
                arrayList.add(new VocabularyEachItem(5, vid, "Speak Out", "آواز اٹھاؤ"));
                arrayList.add(new VocabularyEachItem(6, vid, "Listen", "سنو"));
                arrayList.add(new VocabularyEachItem(7, vid, "Be ready/get ready", "تیار رہو/تیار ہو جاؤ"));
                arrayList.add(new VocabularyEachItem(8, vid, "Wait here", "یہاں انتظار کریں"));
                arrayList.add(new VocabularyEachItem(9, vid, "Don't move", "حرکت نہ کریں"));
                arrayList.add(new VocabularyEachItem(10, vid, "Keep quiet", "خاموشی اختیار کرو"));
                /*arrayList.add(new VocabularyEachItem(11, vid, "Get lost/get out", "کھو جانا/باہر نکلنا"));
                arrayList.add(new VocabularyEachItem(12, vid, "Go straight", "سیدھے جاو"));
                arrayList.add(new VocabularyEachItem(13, vid, "Go up", "اوپر جاؤ"));
                arrayList.add(new VocabularyEachItem(14, vid, "Take it", "لے لو"));
                arrayList.add(new VocabularyEachItem(15, vid, "Come here", "ادھر آو"));
               arrayList.add(new VocabularyEachItem(16, vid, "Go ahead", "آگے بڑھو"));
                arrayList.add(new VocabularyEachItem(17, vid, "Go slowly/walk slowly", "آہستہ چلیں / آہستہ چلیں"));
                arrayList.add(new VocabularyEachItem(18, vid, "Go at once", "فوراً جاؤ"));
                arrayList.add(new VocabularyEachItem(19, vid, "Don’t break it", "اسے مت توڑو"));
                arrayList.add(new VocabularyEachItem(20, vid, "Ask his/her name", "اس کا نام پوچھیں"));
                arrayList.add(new VocabularyEachItem(21, vid, "Go down", "نیچے جاؤ"));
                arrayList.add(new VocabularyEachItem(22, vid, "Don’t go", "مت جاؤ"));
                arrayList.add(new VocabularyEachItem(23, vid, "Come near", "قریب آئیں"));
                arrayList.add(new VocabularyEachItem(24, vid, "Don’t forget", "مت بھولنا"));
                arrayList.add(new VocabularyEachItem(25, vid, "Don’t trouble /tease me", "مجھے پریشان مت کر"));
                arrayList.add(new VocabularyEachItem(26, vid, "Don’t break it", "اسے مت توڑو"));
                arrayList.add(new VocabularyEachItem(27, vid, "Don't come", "مت آنا"));
                arrayList.add(new VocabularyEachItem(28, vid, "Go back", "واپس جاو"));
                arrayList.add(new VocabularyEachItem(29, vid, "Think before you speak", "بولنے سے پہلےسوچو"));
                arrayList.add(new VocabularyEachItem(30, vid, "Come back", "واپس آجاؤ"));
                arrayList.add(new VocabularyEachItem(31, vid, "Move ahead", "آگے بڑھو"));
                arrayList.add(new VocabularyEachItem(32, vid, "Let me work", "مجھے کام کرنے دو"));

                arrayList.add(new VocabularyEachItem(33, vid, "Just listen", "صرف سنو"));
                arrayList.add(new VocabularyEachItem(34, vid, "Take care of", "کا خیال رکھنا"));
                arrayList.add(new VocabularyEachItem(35, vid, "Let him pass", "اسے گزرنے دو"));
                arrayList.add(new VocabularyEachItem(36, vid, "Come soon", "جلدی انا"));
                arrayList.add(new VocabularyEachItem(37, vid, "Let me go", "مجھے جانے دو"));

                arrayList.add(new VocabularyEachItem(38, vid, "Mind your business", "اپنے کام سے کام رکھو"));
                arrayList.add(new VocabularyEachItem(39, vid, "Come on", "چلو بھئی"));
                arrayList.add(new VocabularyEachItem(40, vid, "Be careful", "محتاط رہیں"));
                arrayList.add(new VocabularyEachItem(41, vid, "Don’t cut jokes", "لطیفے نہ کاٹیں"));
                arrayList.add(new VocabularyEachItem(42, vid, "Don’t talk nonsense", "فضول باتیں نہ کرو"));
                arrayList.add(new VocabularyEachItem(43, vid, "Never mind", "کوئی بات نہیں / پرواہ نہ کرو "));
                arrayList.add(new VocabularyEachItem(44, vid, "Don’t delay", "تاخیر نہ کریں"));
                arrayList.add(new VocabularyEachItem(45, vid, "Never forget", "کبھی مت بھولنا"));
                arrayList.add(new VocabularyEachItem(46, vid, "Don’t worry", "فکر نہ کرو"));
                arrayList.add(new VocabularyEachItem(47, vid, "Don’t tease", "تنگ نہ کرو"));
                arrayList.add(new VocabularyEachItem(48, vid, "Don’t be late", "دیر نہ کریں"));
                arrayList.add(new VocabularyEachItem(49, vid, "Please wake him up", "پلیز اسے جگا دو"));
                arrayList.add(new VocabularyEachItem(50, vid, "Don’t be silly", "بیوقوف نہ بنو"));
                arrayList.add(new VocabularyEachItem(51, vid, "Please be seated", "براہ کرم بیٹھ جائیں"));
                arrayList.add(new VocabularyEachItem(52, vid, "Please wait a  bit", "براہ کرم تھوڑا انتظار کریں"));
                arrayList.add(new VocabularyEachItem(53, vid, "Leave it", "اسے چھوڑ دو"));
                arrayList.add(new VocabularyEachItem(54, vid, "Take this dose", "یہ خوراک لیں"));
                arrayList.add(new VocabularyEachItem(55, vid, "Please keep quite", "پلیز چپ رہو"));
                arrayList.add(new VocabularyEachItem(56, vid, "Follow me/come with me", "میرے ساتھ چلو/میرے ساتھ چلو"));
                arrayList.add(new VocabularyEachItem(57, vid, "Please allow me to go", "براہ کرم مجھے جانے کی اجازت دیں"));
                arrayList.add(new VocabularyEachItem(58, vid, "Keep your house clean", "اپنے گھر کو صاف ستھرا رکھیں"));
                arrayList.add(new VocabularyEachItem(59, vid, "Come to the point", "بات پر آؤ"));*/
                break;
            }
            case 1: {
                arrayList.add(new VocabularyEachItem(0, vid, "All the Best", "سب سے بہترین"));
                arrayList.add(new VocabularyEachItem(1, vid, "Have a nice luck!", "اچھی قسمت ہے"));
                arrayList.add(new VocabularyEachItem(2, vid, "With Best Wishes", "نیک خواہشات کے ساتھ"));
                arrayList.add(new VocabularyEachItem(3, vid, "Wish, best wishes!", "کاش، نیک تمنائیں!"));
                arrayList.add(new VocabularyEachItem(4, vid, "May your dreams come true", "آپ کے خواب پورے ہوں"));
                arrayList.add(new VocabularyEachItem(5, vid, "Praying for your future.", "مستقبل کیلئے دعا کرتے ہیں۔"));
                arrayList.add(new VocabularyEachItem(6, vid, "Wishing you health and well-being.", "آپ کو صحت و تندرستی کی دعا۔"));
                arrayList.add(new VocabularyEachItem(7, vid, "May success be always with you.", "کامیابی آپ کی قدموں میں ہمیشہ ہمراہ ہو۔"));
                arrayList.add(new VocabularyEachItem(8, vid, "Heartfelt congratulations to you.", "آپ کو دل سے مبارکباد۔"));
                arrayList.add(new VocabularyEachItem(9, vid, " May you shine in the light of success.", "آپ کو کامیابی کی روشنی میں چمکتے رہیں"));
                arrayList.add(new VocabularyEachItem(10, vid, "Blessings are with you.", "دعائیں آپ کے ساتھ ہیں۔"));

                break;
            }
            case 2: {
                arrayList.add(new VocabularyEachItem(0, vid, "It's nice of you.", "یہ آپ کی اچھی بات ہے۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Thank You", "شکریہ"));
                arrayList.add(new VocabularyEachItem(2, vid, "Cheers", "شاباش!"));
                arrayList.add(new VocabularyEachItem(3, vid, "Ameen", "آمین"));
                arrayList.add(new VocabularyEachItem(4, vid, " I am grateful for your prayers.", "دعاؤں کے لئے آپ کا شکرگزار ہوں۔"));
                arrayList.add(new VocabularyEachItem(5, vid, "Your prayers have strengthened me.", "آپ کی دعاؤں نے مجھے مستحکم کیا۔"));
                arrayList.add(new VocabularyEachItem(6, vid, "Thank you for honoring my prayers.", "دعاؤں کا احترام کرتے ہوئے آپ کا شکریہ۔"));
                arrayList.add(new VocabularyEachItem(7, vid, "Your prayers touched my heart.", "آپ کی دعاؤں نے میرے دل کو چھوا۔"));
                arrayList.add(new VocabularyEachItem(8, vid, "There is nothing equal to your prayers.", "آپ کی دعاؤں کے برابر کچھ نہیں۔"));
                arrayList.add(new VocabularyEachItem(9, vid, "Thank you for expressing your prayers.", "دعاؤں کا اظہار کرنے پر آپ کا شکریہ۔"));
                arrayList.add(new VocabularyEachItem(10, vid, "Your love is equivalent to blessings.", "آپ کی محبت برکت کے برابر ہے"));
                /*arrayList.add(new VocabularyEachItem(11, vid, "Ameen", "آمین"));*/

                break;
            }
            case 3: {
                arrayList.add(new VocabularyEachItem(0, vid, "Why don't you join us in...?", "آپ ہمارے ساتھ کیوں نہیں آتے....؟"));
                arrayList.add(new VocabularyEachItem(1, vid, "I'd love you invite you.", "میں آپ کو دعوت دینا پسند کروں گا"));
                arrayList.add(new VocabularyEachItem(2, vid, "Come and share the moments, please!", "آؤ اور لمحات کا اشتراک کریں، براہ کرم!"));
                arrayList.add(new VocabularyEachItem(3, vid, "Please, accept my invitation", "براہ کرم، میری دعوت قبول کریں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "You are cordially invited", "آپ کو دل کی گہرائیوں سے مدعو کیا جاتا ہے۔"));
                arrayList.add(new VocabularyEachItem(5, vid, "Come, join us.", "آئیے، ہمارے ساتھ ملیے۔"));
                arrayList.add(new VocabularyEachItem(6, vid, "Please come to our place.", "برائے مہربانی ہمارے پاس آئیے۔"));
                arrayList.add(new VocabularyEachItem(7, vid, "You are invited by us.", "آپ کو ہماری مدعوت ہے۔"));
                arrayList.add(new VocabularyEachItem(8, vid, "We invite you for some entertainment.", "ہم آپ کو تفریح کیلئے بلاتے ہیں۔"));
                arrayList.add(new VocabularyEachItem(9, vid, "You are warmly welcomed by us.", "آپ کو ہماری طرف سے خوش آمدید ہے۔"));
                arrayList.add(new VocabularyEachItem(10, vid, "Please accept our invitation.", "برائے مہربانی ہماری مدعوت قبول کریں۔۔"));
                break;
            }
            case 4: {
                arrayList.add(new VocabularyEachItem(0, vid, "Oh! I'd love to...", "اوہ! مجھے پسند آئے گا...."));
                arrayList.add(new VocabularyEachItem(1, vid, "I do come", "میں آتا ہوں"));
                arrayList.add(new VocabularyEachItem(2, vid, "Yes, it is irresistible.", "جی ہاں، یہ ناقابل برداشت ہے."));
                arrayList.add(new VocabularyEachItem(3, vid, "That sounds great", "یہ بہت اچھا لگتا ہے."));
                arrayList.add(new VocabularyEachItem(4, vid, "i won't say no!", "میں نہیں کہوں گا۔"));
                arrayList.add(new VocabularyEachItem(5, vid, "I accept your invitation. Heartfelt thanks!", "میں آپ کی مدعوت قبول کرتا ہوں۔ دلی شکریہ!"));
                arrayList.add(new VocabularyEachItem(6, vid, "I'm thrilled to be included. ", "بہت خوشی ہوئی کہ آپ نے مجھے شامل کیا۔۔"));
                arrayList.add(new VocabularyEachItem(7, vid, " I'm sorry, it's not suitable for me. ", "عذرت، میرے لئے مناسب نہیں ہے۔"));
                break;
            }
            case 5: {
                arrayList.add(new VocabularyEachItem(0, vid, "Thank you, but...", "شکریہ لیکن ......"));
                arrayList.add(new VocabularyEachItem(1, vid, "I am afraid , I can't come", "مجھے ڈر ہے، میں نہیں آ سکتا"));
                arrayList.add(new VocabularyEachItem(2, vid, "I wish, I could but....", "کاش، میں کر سکتا لیکن...."));
                arrayList.add(new VocabularyEachItem(3, vid, "Thanks for asking but....", "پوچھنے کا شکریہ لیکن...."));
                arrayList.add(new VocabularyEachItem(4, vid, "I am not feeling good to reject you but.....", "مجھے تمھیں ٹھکرانا اچھا نہیں لگتا لیکن..."));

                break;
            }
            case 6: {
                arrayList.add(new VocabularyEachItem(0, vid, "You are looking smart.", "آپ سمارٹ لگ رہے ہیں۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "What a nice you've done", "کیا اچھا کیا تم نے"));
                arrayList.add(new VocabularyEachItem(2, vid, "You look great today", "آپ آج بہت اچھے لگ رہے ہیں۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "Pay my compliments to him", "اس کو میری داد دیں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "how pretty you are!", "تم کتنی خوبصورت ہو!"));
                break;
            }
            case 7: {
                arrayList.add(new VocabularyEachItem(0, vid, "You deserve it.", "آپ اس کے مستحق ہیں."));
                arrayList.add(new VocabularyEachItem(1, vid, "Congratulation!", "مبارک ہو!"));
                arrayList.add(new VocabularyEachItem(2, vid, "Fantastic!", "لاجواب!"));
                arrayList.add(new VocabularyEachItem(3, vid, " I congratulation you", "میں آپ کو مبارکباد دیتا ہوں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "Please accept my congratulations.", "براہ کرم میری مبارکباد قبول کریں۔"));
                break;
            }
            case 8: {
                arrayList.add(new VocabularyEachItem(0, vid, "Thank you very much.", "بہت بہت شکریہ."));
                arrayList.add(new VocabularyEachItem(1, vid, "Thanks for your compliments.", "آپ کی تعریف کے لئے شکریہ"));
                arrayList.add(new VocabularyEachItem(2, vid, "Nice of you to say so.", "آپ کا یہ کہنا اچھا لگا"));
                arrayList.add(new VocabularyEachItem(3, vid, "I loved to hear it from you.", "مجھے تم سے یہ سننا اچھا لگا۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "It is due to your good wishes.", "یہ آپ کی نیک خواہشات کی وجہ سے ہے۔"));

                break;
            }
            case 9: {
                arrayList.add(new VocabularyEachItem(0, vid, "I am extremely sorry.", "مجھے انتہائی افسوس ہے۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Pardon me.", "مجھے معاف کر دو۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "Please accept my apologies.", "برائے مہربانی میری معزرت قبول کریں"));
                arrayList.add(new VocabularyEachItem(3, vid, "I beg your pardon.", "میں آپ سے معافی مانگتا ہوں"));
                arrayList.add(new VocabularyEachItem(4, vid, "I am really sorry.", "میں واقعی شرمندہ ہوں."));
                break;
            }
            case 10: {
                arrayList.add(new VocabularyEachItem(0, vid, "No Problem, leave it.", "کوئی مسئلہ نہیں، چھوڑ دو۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Don't mention.", "ذکر نہ کرو۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "Not at all.", "بالکل نہیں."));
                arrayList.add(new VocabularyEachItem(3, vid, "No need to apologize.", "معذرت کی ضرورت نہیں."));
                arrayList.add(new VocabularyEachItem(4, vid, "Forget it.", "اسے بھول جاؤ."));
                break;
            }
            case 11: {
                arrayList.add(new VocabularyEachItem(0, vid, "It is sad to know.", "یہ جان کر دکھ ہوا۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Have patience. Everything will be ok.", "حوصلہ رکھو. سب کچھ ٹھیک ہو جائے گا."));
                arrayList.add(new VocabularyEachItem(2, vid, "I am deeply shocked, too.", "مجھے بھی شدید صدمہ ہوا ہے۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "Let me share the hard moments.", "مجھے مشکل لمحات بانٹنے دو۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "May God help you in this situation.", "خدا اس صورتحال میں آپ کی مدد کرے۔"));
                break;
            }
            case 12: {
                arrayList.add(new VocabularyEachItem(0, vid, "Excuse me, I'll be back within a minute.", "معاف کیجئے گا، میں ایک منٹ میں واپس آ جاؤں گا۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Excuse me for a moment.", "ایک لمحے کے لیے معاف کیجئے گا۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "Please don't you go, I'm coming.", "پلیز آپ مت جائیں میں آ رہا ہوں۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "Rank in a moment.", "ایک لمحے میں درجہ بندی کریں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "Stay a while, I'm back.", "تھوڑی دیر ٹھہرو میں واپس آ گیا ہوں۔"));
                break;
            }
            case 13: {
                arrayList.add(new VocabularyEachItem(0, vid, "That's why...", "اس لیے"));
                arrayList.add(new VocabularyEachItem(1, vid, "In fact....", "حقیقت میں...."));
                arrayList.add(new VocabularyEachItem(2, vid, "Why is not so, because....", "ایسا کیوں نہیں ہے، کیونکہ...."));
                arrayList.add(new VocabularyEachItem(3, vid, "You see, It is like.....", "تم نے دیکھا، یہ اس طرح ہے ..."));
                arrayList.add(new VocabularyEachItem(4, vid, "Now listen to me....", "اب میری بات سنو...."));
                break;
            }
            case 14: {
                arrayList.add(new VocabularyEachItem(0, vid, "Is this right or ...", "کیا یہ صحیح ہے یا...."));
                arrayList.add(new VocabularyEachItem(1, vid, "What is your intention?", "آپ کا کیا ارادہ ہے؟"));
                arrayList.add(new VocabularyEachItem(2, vid, "So, are you ready to...?", "تو کیا آپ تیار ہیں...؟"));
                arrayList.add(new VocabularyEachItem(1, vid, "Are you convinced?", "کیا آپ قائل ہیں؟"));
                arrayList.add(new VocabularyEachItem(2, vid, "What you think.....", "تم کیا سوچتے ہو....."));
                break;
            }
            case 15: {
                arrayList.add(new VocabularyEachItem(0, vid, "Yes, I agree.", "ہاں میں متفق ہوں."));
                arrayList.add(new VocabularyEachItem(1, vid, "You made me convinced.", "آپ نے مجھے قائل کر دیا ہے۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "No other option.", "کوئی اور آپشن نہیں ہے۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "I am thinking the same way.", "میں بھی یہی سوچ رہا ہوں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "Yes, of course.", "جی بلکل."));
                break;
            }
            case 16: {
                arrayList.add(new VocabularyEachItem(0, vid, "I think different.", "میں مختلف سوچتا ہوں"));
                arrayList.add(new VocabularyEachItem(1, vid, "I am not Convinced.", "میں قائل نہیں ہوں۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "No, it is not possible.", "نہیں، یہ ممکن نہیں ہے۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "I'm not with you at all.", "میں آپ کے ساتھ بالکل نہیں ہوں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "I don't agree.", "میں نہیں مانتا۔"));
                break;
            }
            case 17: {
                arrayList.add(new VocabularyEachItem(0, vid, "I am not entirely convinced but...", "میں پوری طرح سے قائل نہیں ہوں لیکن ...."));
                arrayList.add(new VocabularyEachItem(1, vid, "I know you're right but.....", "میں جانتا ہوں کہ تم ٹھیک ہو لیکن....."));
                arrayList.add(new VocabularyEachItem(2, vid, "I can see but....", "میں دیکھ سکتا ہوں لیکن...."));
                arrayList.add(new VocabularyEachItem(3, vid, "I am not completely satisfied.", "میں مکمل طور پر مطمئن نہیں ہوں."));
                arrayList.add(new VocabularyEachItem(4, vid, "OK but first clear some ambiguities.", "ٹھیک ہے لیکن پہلے کچھ ابہام صاف کریں۔"));
                break;
            }
            case 18: {
                arrayList.add(new VocabularyEachItem(0, vid, "A very happy day to you.", "آپ کے لیے ایک بہت ہی مبارک دن۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Many happy returns of the day.", "دن کی بہت ساری مبارک واپسی۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "I wish you on the occassion of....", "میں آپ کو اس موقع پر مبارکباد پیش کرتا ہوں...."));
                arrayList.add(new VocabularyEachItem(3, vid, "May this event bring happiness to you.", "یہ واقعہ آپ کے لیے خوشیاں لائے۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "A very special happy birthday to you.", "آپ کو سالگرہ بہت خاص مبارک ہو۔"));
                break;
            }
            case 19: {
                arrayList.add(new VocabularyEachItem(0, vid, "To you, too.", "آپ کو بھی۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Lovely to hear it from you.", "آپ سے یہ سن کر بہت اچھا لگا۔"));
                arrayList.add(new VocabularyEachItem(2, vid, "Thank you and same from me too.", "شکریہ اور میری طرف سے بھی۔"));
                arrayList.add(new VocabularyEachItem(3, vid, "You made my day....", "تم نے میرا دن بنا دیا...."));
                arrayList.add(new VocabularyEachItem(4, vid, "Thank you.", "شکریہ"));
                break;
            }
            case 20: {
                arrayList.add(new VocabularyEachItem(0, vid, "Thank you, I like it very much.", "شکریہ، مجھے یہ بہت پسند ہے۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "Who can refuse?", "کون انکار کر سکتا ہے؟"));
                arrayList.add(new VocabularyEachItem(2, vid, "Smashing!", "توڑ پھوڑ!"));
                arrayList.add(new VocabularyEachItem(3, vid, "You are very nice.", "آپ بہت اچھے ہیں۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "I'd love to.", "میں پسند کروں گا۔"));
                break;

            }
            case 21: {
                arrayList.add(new VocabularyEachItem(0, vid, "I am sorry but I can't take.", "مجھے افسوس ہے لیکن میں نہیں لے سکتا۔"));
                arrayList.add(new VocabularyEachItem(1, vid, "No, thank you.", "نہیں شکریہ."));
                arrayList.add(new VocabularyEachItem(2, vid, "Mind, if I say 'No'.", "ذہن میں رکھو، اگر میں 'نہیں' کہوں."));
                arrayList.add(new VocabularyEachItem(3, vid, "Not this time, Please.", "اس بار نہیں، براہ مہربانی."));
                arrayList.add(new VocabularyEachItem(4, vid, "Some other day , Please.", "کسی اور دن، براہ مہربانی."));
                break;
            }
            case 22: {
                arrayList.add(new VocabularyEachItem(0, vid, "Do you have some information about...?", "کیا آپ کے پاس اس بارے میں کچھ معلومات ہیں...؟"));
                arrayList.add(new VocabularyEachItem(1, vid, "Would you tell me,.....please?", "کیا آپ مجھے بتائیں گے، براہ مہربانی؟"));
                arrayList.add(new VocabularyEachItem(2, vid, "If you could tell me?", "اگر آپ مجھے بتا سکتے"));
                arrayList.add(new VocabularyEachItem(3, vid, "Excuse me, would you let me know about me ...?", "معاف کیجئے گا، کیا آپ مجھے میرے بارے میں بتائیں گے...؟"));
                arrayList.add(new VocabularyEachItem(4, vid, "Will you help me in...?", "کیا آپ اس میں میری مدد کریں گے...؟"));
                break;
            }
            case 23: {
                arrayList.add(new VocabularyEachItem(0, vid, "Just Wait a minute...", "بس ایک منٹ لیں...."));
                arrayList.add(new VocabularyEachItem(1, vid, "Well, actually....", "ٹھیک ہے، اصل میں ...."));
                arrayList.add(new VocabularyEachItem(2, vid, "Listen to me, now...", "میری بات سنو اب..."));
                arrayList.add(new VocabularyEachItem(3, vid, "Why don't you consider.....", "تم غور کیوں نہیں کرتے......"));
                arrayList.add(new VocabularyEachItem(4, vid, "Ponder it again.", "اس پر دوبارہ غور کریں۔"));
                break;
            }
            case 24: {
                arrayList.add(new VocabularyEachItem(0, vid, "Just consider, if...", "ذرا غور کریں، اگر...."));
                arrayList.add(new VocabularyEachItem(1, vid, "Think over the results!", "نتائج پر غور کریں!"));
                arrayList.add(new VocabularyEachItem(2, vid, "Imagine if....", "سوچو اگر...."));
                arrayList.add(new VocabularyEachItem(3, vid, "It is horrible to think of next.", "اگلے کے بارے میں سوچنا بھیانک ہے۔"));
                arrayList.add(new VocabularyEachItem(4, vid, "What if....", "کیا اگر...."));
                break;
            }
            case 25: {
                arrayList.add(new VocabularyEachItem(0, vid, "I'd like to remind you...", "میں آپ کو یاد دلانا چاہوں گا..."));
                arrayList.add(new VocabularyEachItem(1, vid, "Did you forget again?", "کیا آپ پھر بھول گئے؟"));
                arrayList.add(new VocabularyEachItem(2, vid, "Do you have a picture of...?", "کیا آپ کے پاس تصویر ہے...؟"));
                arrayList.add(new VocabularyEachItem(3, vid, "Do you still remember....?", "کیا تمہیں اب بھی یاد ہے....؟"));
                arrayList.add(new VocabularyEachItem(4, vid, "can you recall..?", "کیا آپ یاد کر سکتے ہیں..؟"));
                break;
            }
        }
        return arrayList;
    }
}