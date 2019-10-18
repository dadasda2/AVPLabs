package lab2.externserv.server.handler;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import test.handler.Message;

import java.util.List;

public class MessageToTextFrameHandler extends MessageToMessageEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(msg);
		out.add(new TextWebSocketFrame(json));
	}

}
