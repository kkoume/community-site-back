package kr.communityserver.Handler;

import kr.communityserver.entity.Chat;
import kr.communityserver.repository.ChatRepository;
import kr.communityserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketHandler extends TextWebSocketHandler {
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        String [] parts = msg.split("\\*");
        log.info("이거봐라 이거");
        if(parts[0].equals("fileUpload")){
            Chat chat = chatRepository.findById(Integer.parseInt(parts[1])).get();
            msg=
                   "file"+ "*"+
                           chat.getOName()+"*"
                    +chat.getSName()+"*"+
                           chat.getUserId() + "*"+chat.getLocalDateTime()
            +"*"+chat.getChatRoom()+"*"+chat.getMessage();
        }else{
            //chatService.saveChat(msg);

        }
        log.info(msg);
        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                wss.sendMessage(new TextMessage(msg));
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        log.info("here//");
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        log.info("here2//");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}

