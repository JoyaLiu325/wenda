<#include "header.ftl"/>
<link rel="stylesheet" href="../styles/letter.css">
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <ul class="letter-list">
                <#list messageList as message>
                <li id="conversation-item-10001_622873">
                <a class="letter-link" href="/msg/detail?conversationId=${message.message.conversationId}">
                </a>
                <div class="letter-info">
                <span class="l-time">${message.message.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                <div class="l-operate-bar">
                    <!--
                <a href="javascript:void(0);" class="sns-action-del" data-id="10001_622873">
                		删除
                </a>
                -->
                <a href="/msg/detail?conversationId=${message.message.conversationId}">
                 共有${message.message.id}条消息
                </a>
                </div>
                </div>
                <div class="chat-headbox">
                <span class="msg-num">
                ${message.unRead}
                </span>
                <a class="list-head">
                <img alt="ͷ��" src="${message.user.headUrl}">
                </a>
                </div>
                <div class="letter-detail">
                <a title="${message.user.name} " href="/user/${message.user.id}" class="letter-name level-color-1">
                    ${message.user.name}
                </a>
                <p class="letter-brief">
                <a href="/msg/detail?conversationId=${message.message.conversationId}">
               	 ${message.message.content}
                </a>
                </p>
                </div>
                </li>
                </#list>
            </ul>

        </div>
    </div>
<#include "footer.ftl"/>