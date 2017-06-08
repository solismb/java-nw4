#include <stdio.h>
#include <math.h>
#define N 16
#define M 3

typedef struct table {
	unsigned int addr;
	int prefix;
	char next[N];
} TABLE;

unsigned setAddr(char temp[])
{
	int ad[4], i, p = 24;
	unsigned int addr = 0;

	sscanf(temp, "%d.%d.%d.%d", &ad[0], &ad[1], &ad[2], &ad[3]);

	for (i = 0; i < 4; i++) {
		addr += ad[i] << p;
		p -= 8;
	}

	return addr;
}

int main()
{
	TABLE table[M] =
	{
		{setAddr("10.133.94.0"), 24, "10.133.94.254"},
		{setAddr("202.23.192.0"), 21, "202.23.192.1"},
		{setAddr("0.0.0.0"), 0, "1.2.3.4"}
	};
	char input[N] = {'\0'};
	unsigned int inaddr, nwaddr, i = 0, flag = 0;

	printf("宛先IPアドレスを入力して下さい\nIP : ");
	inaddr = setAddr(fgets(input, N, stdin));

	while(i < 3) {
		nwaddr = ~((int)pow(2, 32 - table[i].prefix) - 1) & inaddr;
		if (nwaddr == table[i].addr) {
			printf("\n次の送り先は\n  %s\nです.\n", table[i].next);
			flag = 1;
			break;
		} else {
			i++;
		}
	}

	if (!flag)
		printf("次の送り先は\n  %s\nです.\n", table[i].next);

	return 0;
}
	
