#include <iostream>
#include <sstream>
#include <cstdio>
#include <cstring>
using namespace std;

char tmp[1000111];
char in_file[88], out_file[88];

int main(int argc, char* argv[]) {
    strcat(in_file, "data/");
    strcat(in_file, argv[1]);

    strcat(out_file, "1000_rows/");
    strcat(out_file, argv[1]);

    freopen(in_file, "r", stdin);
    freopen(out_file, "w", stdout);

    cerr << "in: " << in_file << endl;
    cerr << "out: " << out_file << endl;

    int N = 1000 + 1;

    for(int i = 0; i < N; ++i) {
        gets(tmp);
        cout << tmp << endl;
    }
}
