asakusafw-example-sequence

# 概要

Asakusa Framework 0.10.1でOracleシーケンスを呼ぶサンプル


# 使用方法

src/main/libs/ にojdbc.jarを配置して下さい。

core/conf/asakusa-resources.xmlにJDBC接続の設定を書きます。

プログラムからは、SequenceApi.nextLong(名前)を呼び出します。
この名前は、asakusa-resources.xmlから設定を取得する際に使用します。

例えば"seqTest"にすると、asakusa-resources.xmlのsequence.seqTest.class等の設定を使用します。
OracleSequenceDelegateの場合、実際のOracleシーケンス名をsequence.seqTest.sequenceNameで記述します。
sequence.seqTest.urlの代わりにsequence.urlで共通の設定を記述することが出来ます。


# テスト実行方法

テスト実行用のASAKUSA_HOMEのasakusa-resources.xmlのsequence.seqTest.classにLocalSequenceDelegateを指定することで、
実際にはDBアクセスせず、AtomicLongを使用したカウンターを使うことが出来ます。

