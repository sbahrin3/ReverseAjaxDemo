package test.push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.dwr.Util;

public class DemoVoter {
	
	private List<Player> players = new ArrayList<Player>();
	
	public DemoVoter() {
		players.add(new Player("Transformers: Age of Extinsion"));
		players.add(new Player("Captain America: The Winter Soldier"));
		players.add(new Player("Guardians of the Galaxy "));
		players.add(new Player("A Walk Among the Tombstones"));
		players.add(new Player("Teenage Mutant Ninja Turtles"));
		players.add(new Player("X-Men: Days of Future Past"));
	}
	
	public void callReverseAjax() {
		
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String attributeName = "voter";
		scriptSession.setAttribute(attributeName, true);
		
		update();
		

	}
	
	public void callVote(String name) {
		for ( Player p : players ) {
			if ( p.getName().equals(name)) {
				p.vote();
				break;
			}
		}
		update();
	}
	
	public void update() {
		String page = ServerContextFactory.get().getContextPath() + "/push/voter.html";
		ScriptSessionFilter filter = new MyScriptSessionFilter("voter");
		Collections.sort(players, new PlayerComparator());
		Browser.withPageFiltered(page, filter, new Runnable() {
			public void run() {
				
				int cnt = 0;
				String s = "<table>";
				s += "<tr style=\"height:30px;\"><td style=\"width:40px\"></td><td style=\"width:400px;border-bottom:1px dotted #000\">Name</td><td style=\"width:100px;border-bottom:1px dotted #000;text-align:center\">Vote</td></tr>";
				
				for ( Player p : players ) {
					cnt++;
					s += "<tr style=\"height:30px\"><td>" + cnt + ")</td><td style=\"cursor:pointer\" onclick=\"selectPlayer('" + p.getName() + "');\">" + p.getName() + "</td><td style=\"text-align:center\">" + p.getRank() + "</td></tr>";
				}
				s += "</table>";
				
				Util.setValue("div1", s);

			}
		});
	}
	
	static class PlayerComparator implements Comparator<Player> {
		@Override
		public int compare(Player o1, Player o2) {
			if ( o1.getRank() == o2.getRank() ) return 0;
			return o1.getRank() < o2.getRank() ? 1 : -1;
		}
	}

}
