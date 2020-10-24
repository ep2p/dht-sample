package com.github.ep2p.dht;

import com.github.ep2p.kademlia.Common;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DhtApplication {

	public static void main(String[] args) {
		Common.IDENTIFIER_SIZE=4;
		Common.LAST_SEEN_SECONDS_TO_CONSIDER_ALIVE = 10;
		SpringApplication.run(DhtApplication.class, args);
	}

}
