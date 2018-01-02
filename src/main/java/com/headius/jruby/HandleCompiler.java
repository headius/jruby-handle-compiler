package com.headius.jruby;

import com.headius.invokebinder.Binder;
import org.jruby.Ruby;
import org.jruby.ast.*;
import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.embed.io.ReaderInputStream;
import org.jruby.parser.StaticScope;
import org.jruby.runtime.builtin.IRubyObject;

import java.io.StringReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

/**
 * Compiles a Ruby AST into {@link java.lang.invoke.MethodHandle}.
 */
public class HandleCompiler implements NodeVisitor<MethodHandle> {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private final Ruby runtime;

    public static void main(String[] args) throws Throwable {
        Ruby runtime = Ruby.newInstance();

        Node top = (Node) runtime.parseFromMain("main", new ReaderInputStream(new StringReader(args[0])));

        System.out.println("AST:\n" + top);

        HandleCompiler hc = new HandleCompiler(runtime);

        MethodHandle handle = hc.compile(top);

        Object result = handle.invoke();

        System.out.println("result: " + result);
    }

    public HandleCompiler(Ruby runtime) {
        this.runtime = runtime;
    }

    MethodHandle compile(Node node) {
        return node.accept(this);
    }

    @Override
    public MethodHandle visitAliasNode(AliasNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitAndNode(AndNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitArgsNode(ArgsNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitArgsCatNode(ArgsCatNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitArgsPushNode(ArgsPushNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitArgumentNode(ArgumentNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitArrayNode(ArrayNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitAttrAssignNode(AttrAssignNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBackRefNode(BackRefNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBeginNode(BeginNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBignumNode(BignumNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBlockArgNode(BlockArgNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBlockNode(BlockNode iVisited) {
        Node[] children = iVisited.children();

        MethodHandle[] handles =
                Arrays
                        .stream(children)
                        .map(node -> compile(node))
                        .toArray(n -> new MethodHandle[n]);

        return Binder.from(Object.class, Object[].class)
                .permute(new int[handles.length])
                .filterForward(0, handles)
                .drop(0, handles.length - 1)
                .identity();
    }

    @Override
    public MethodHandle visitBlockPassNode(BlockPassNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitBreakNode(BreakNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitConstDeclNode(ConstDeclNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitClassVarAsgnNode(ClassVarAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitClassVarDeclNode(ClassVarDeclNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitClassVarNode(ClassVarNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitCallNode(CallNode iVisited) {
        List<Node> nodes = iVisited.getArgsNode().childNodes();

        switch (iVisited.getName()) {
            case "+":
                return Binder.from(Object.class, Object[].class)
                        .permute(new int[nodes.size() + 1])
                        .filter(0, compile(iVisited.getReceiverNode()))
                        .filterForward(1, nodes.stream().map(node -> compile(node)).toArray(n -> new MethodHandle[n]))
                        .invokeStaticQuiet(LOOKUP, HandleCompiler.class, "add");
        }

        throw new RuntimeException();
    }

    public static Object add(Object a, Object b) {
        if (a instanceof Long) {
            if (b instanceof Long) {
                return (Long) a + (Long) b;
            }
        }
        throw new RuntimeException((a == null ? "null" : a.getClass()) + ", " + (b == null ? "null" : b.getClass()));
    }

    @Override
    public MethodHandle visitCaseNode(CaseNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitClassNode(ClassNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitColon2Node(Colon2Node iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitColon3Node(Colon3Node iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitComplexNode(ComplexNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitConstNode(ConstNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDAsgnNode(DAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDRegxNode(DRegexpNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDStrNode(DStrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDSymbolNode(DSymbolNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDVarNode(DVarNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDXStrNode(DXStrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDefinedNode(DefinedNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDefnNode(DefnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDefsNode(DefsNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitDotNode(DotNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitEncodingNode(EncodingNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitEnsureNode(EnsureNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitEvStrNode(EvStrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitFCallNode(FCallNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitFalseNode(FalseNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitFixnumNode(FixnumNode iVisited) {
        return Binder.from(Object.class, Object[].class)
                .drop(0)
                .constant(Long.valueOf(iVisited.getValue()));
    }

    @Override
    public MethodHandle visitFlipNode(FlipNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitFloatNode(FloatNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitForNode(ForNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitGlobalAsgnNode(GlobalAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitGlobalVarNode(GlobalVarNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitHashNode(HashNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitInstAsgnNode(InstAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitInstVarNode(InstVarNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitIfNode(IfNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitIterNode(IterNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitKeywordArgNode(KeywordArgNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitKeywordRestArgNode(KeywordRestArgNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitLambdaNode(LambdaNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitListNode(ListNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitLiteralNode(LiteralNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitLocalAsgnNode(LocalAsgnNode iVisited) {
        return Binder.from(Object.class, Object[].class)
                .fold(compile(iVisited.getValueNode()))
                .append(iVisited.getIndex())
                .permute(1, 2, 0)
                .foldVoid(Binder.from(void.class, Object[].class, int.class, Object.class).arraySet())
                .drop(0, 2)
                .identity();
    }

    @Override
    public MethodHandle visitLocalVarNode(LocalVarNode iVisited) {
        return Binder.from(Object.class, Object[].class)
                .append(iVisited.getIndex())
                .invoke(MethodHandles.arrayElementGetter(Object[].class));
    }

    @Override
    public MethodHandle visitMultipleAsgnNode(MultipleAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitMatch2Node(Match2Node iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitMatch3Node(Match3Node iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitMatchNode(MatchNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitModuleNode(ModuleNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitNewlineNode(NewlineNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitNextNode(NextNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitNilNode(NilNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitNthRefNode(NthRefNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOpElementAsgnNode(OpElementAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOpAsgnNode(OpAsgnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOpAsgnAndNode(OpAsgnAndNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOpAsgnConstDeclNode(OpAsgnConstDeclNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOpAsgnOrNode(OpAsgnOrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOptArgNode(OptArgNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOrNode(OrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitPreExeNode(PreExeNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitPostExeNode(PostExeNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRationalNode(RationalNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRedoNode(RedoNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRegexpNode(RegexpNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRequiredKeywordArgumentValueNode(RequiredKeywordArgumentValueNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRescueBodyNode(RescueBodyNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRescueNode(RescueNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRestArgNode(RestArgNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRetryNode(RetryNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitReturnNode(ReturnNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitRootNode(RootNode iVisited) {
        Node block = iVisited.getBodyNode();
        StaticScope scope = iVisited.getStaticScope();
        int varCount = scope.getNumberOfVariables();

        MethodHandle child = compile(block);

        MethodHandle combiner = Binder.from(Object[].class)
                .append(varCount)
                .invoke(MethodHandles.arrayConstructor(Object[].class));

        return MethodHandles.foldArguments(child, combiner);
    }

    @Override
    public MethodHandle visitSClassNode(SClassNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitSelfNode(SelfNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitSplatNode(SplatNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitStarNode(StarNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitStrNode(StrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitSuperNode(SuperNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitSValueNode(SValueNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitSymbolNode(SymbolNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitTrueNode(TrueNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitUndefNode(UndefNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitUntilNode(UntilNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitVAliasNode(VAliasNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitVCallNode(VCallNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitWhenNode(WhenNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitWhileNode(WhileNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitXStrNode(XStrNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitYieldNode(YieldNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitZArrayNode(ZArrayNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitZSuperNode(ZSuperNode iVisited) {
        return null;
    }

    @Override
    public MethodHandle visitOther(Node iVisited) {
        return null;
    }
}
