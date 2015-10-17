#include <iostream>
#include <sstream>
#include <cstdio>
#include <cstring>
using namespace std;

char tmp[1000111];

int main(int argc, char* argv[]) {
    freopen(argv[1], "r", stdin);

    char out_file[88];
    memset(out_file, 0, sizeof out_file);
    strcat(out_file, "1000_rows/");
    strcat(out_file, argv[1]);

    cout << out_file << endl;
    freopen(out_file, "w", stdout);

    int N = 1000 + 1;

    for(int i = 0; i < N; ++i) {
        gets(tmp);
        cout << tmp << endl;
    }
}
