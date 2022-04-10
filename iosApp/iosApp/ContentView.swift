import SwiftUI
import shared

struct ContentView: View {
    
    @State private var message = "test"

	var body: some View {
        Text(self.message).onAppear {
            API().getDaigo(target: "努力大事") { (res) in
                self.message = res.text
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
